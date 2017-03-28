package com.xxx.rpclite.utils;

import com.xxx.rpclite.exceptions.ClientException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by klose on 2017/1/30.
 */
public class AddressUtil {

  public static String[] fromatAddress(String remoteAddress) {
    try {
      remoteAddress = remoteAddress.trim();
      if (remoteAddress.contains(",")) {
        String[] hosts = remoteAddress.split(",");
        return hosts;
      } else {
        return new String[]{remoteAddress};
      }

    } catch (Exception e) {
      throw new ClientException("Remote Address Is Invalid.");
    }
  }

  public static boolean isServerAddress(String serverAddress) {
    Pattern pattern = Pattern.compile("^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9]):\\d{0,5}$");
    Matcher matcher = pattern.matcher(serverAddress);
    return matcher.matches();
  }

}
