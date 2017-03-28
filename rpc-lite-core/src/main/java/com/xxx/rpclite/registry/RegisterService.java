package com.xxx.rpclite.registry;

import com.xxx.rpclite.registry.entity.RegisterInfo;
import com.xxx.rpclite.registry.exceptions.RegDiscoveryException;
import com.xxx.rpclite.registry.zookeeper.CuratorRegisterClient;
import com.xxx.rpclite.utils.AddressUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by klose on 2017/2/7.
 */
public class RegisterService extends CuratorRegisterClient {

  private String clusterAddress;

  private String registerPath;

  private HashSet<RegisterInfo> registerInfos;

  public RegisterService(String clusterAddress, String registerPath, RegisterInfo... registerInfos) {
    if (StringUtils.isEmpty(clusterAddress) || StringUtils.isEmpty(registerPath)) {
      throw new RegDiscoveryException("Illegal Connect String Or Register Path");
    }
    this.clusterAddress = clusterAddress;
    this.registerPath = registerPath;
    if (registerInfos != null && registerInfos.length != 0 && isRegiserInfosValid(registerInfos)) {
      this.registerInfos = new HashSet<>();
      this.registerInfos.addAll(Arrays.asList(registerInfos));
    } else {
      throw new RegDiscoveryException("Illegal Register Infos");
    }
  }

  private boolean isRegiserInfosValid(RegisterInfo[] registerInfos) {
    for (RegisterInfo info : registerInfos){
      if (!AddressUtil.isServerAddress(info.toAddressName())){
        return false;
      }
    }
    return true;
  }

  public void init() {
    super.init(clusterAddress, registerPath, registerInfos);
  }

  public void destroy() {
    super.destroy();
  }
}
