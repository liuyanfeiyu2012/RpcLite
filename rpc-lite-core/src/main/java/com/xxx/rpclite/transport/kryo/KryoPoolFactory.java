package com.xxx.rpclite.transport.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.xxx.rpclite.model.MethodRequest;
import com.xxx.rpclite.model.MethodResponse;

import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * Created by klose on 2017/2/20.
 */
public class KryoPoolFactory {

  private volatile static KryoPoolFactory poolFactory = null;

  private KryoFactory factory = new KryoFactory() {
    public Kryo create() {
      Kryo kryo = new Kryo();
      kryo.setReferences(false);
      kryo.register(MethodRequest.class, new JavaSerializer());
      kryo.register(MethodResponse.class, new JavaSerializer());
      kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
      return kryo;
    }
  };


  private KryoPool pool = new KryoPool.Builder(factory).softReferences().build();

  private KryoPoolFactory() {
  }

  public static KryoPool getKryoPool() {
    if (poolFactory == null) {
      synchronized (KryoPoolFactory.class) {
        if (poolFactory == null) {
          poolFactory = new KryoPoolFactory();
        }
      }
    }
    return poolFactory.getPool();
  }

  public KryoPool getPool() {
    return pool;
  }
}

