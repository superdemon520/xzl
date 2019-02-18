package cn.xinzhili.api.doctor.config;

/**
 * Created by MarlinL on 16/02/2017.
 */
public interface ConfigConsts {

  String API_HOST = "172.31.2.133";

  int API_PORT = 9007;

  String API_CONTEXT = "/api/v0/doctor";

  static String prefixWithContext(String name) {
    return API_CONTEXT + name;
  }

}
