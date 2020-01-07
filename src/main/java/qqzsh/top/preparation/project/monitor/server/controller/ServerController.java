package qqzsh.top.preparation.project.monitor.server.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.project.monitor.server.domain.Server;
import qqzsh.top.preparation.project.monitor.server.domain.Server;

/**
 * 服务器监控
 * 
 * @author zsh
 */
@Controller
@RequestMapping("/monitor/server")
public class ServerController extends BaseController
{
    private String prefix = "monitor/server";

    @RequiresPermissions("monitor:server:view")
    @GetMapping()
    public String server(ModelMap mmap) throws Exception
    {
        Server server = new Server();
        server.copyTo();
        mmap.put("server", server);
        return prefix + "/server";
    }
}
