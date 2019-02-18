package cn.xinzhili.api.doctor.controller;

import cn.xinzhili.api.doctor.service.UserService;
import cn.xinzhili.user.api.RegionInfo;
import cn.xinzhili.xutils.core.http.Response;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by marlinl on 29/03/2017.
 */
@RestController
public class MiscController {

  @Autowired
  private UserService userService;

  /**
   * 获取省市县信息
   *
   * @param pid 省市县id
   */
  @GetMapping(value = "/region")
  public Response getRegionInfo(@RequestParam(value = "pid", defaultValue = "0") Integer pid) {
    List<RegionInfo> list = userService.getRegionList(pid);
    return Response.instanceSuccess().withDataEntry("regions", list);
  }

}
