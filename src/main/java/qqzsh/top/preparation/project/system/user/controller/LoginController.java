package qqzsh.top.preparation.project.system.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import qqzsh.top.preparation.common.utils.DateUtils;
import qqzsh.top.preparation.common.utils.VaptchaMessage;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.project.content.message.domain.Message;
import qqzsh.top.preparation.project.content.message.service.IMessageService;
import qqzsh.top.preparation.project.system.dept.service.IDeptService;
import qqzsh.top.preparation.project.system.user.domain.User;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import qqzsh.top.preparation.common.utils.ServletUtils;
import qqzsh.top.preparation.common.utils.StringUtils;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.common.utils.DateUtils;
import qqzsh.top.preparation.common.utils.ServletUtils;
import qqzsh.top.preparation.common.utils.StringUtils;
import qqzsh.top.preparation.common.utils.VaptchaMessage;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.framework.web.controller.BaseController;
import qqzsh.top.preparation.framework.web.domain.AjaxResult;
import qqzsh.top.preparation.project.content.message.domain.Message;
import qqzsh.top.preparation.project.content.message.service.IMessageService;
import qqzsh.top.preparation.project.system.user.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录验证
 * 
 * @author zsh
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IDeptService deptService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response)
    {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request))
        {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }

        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe,
                                @RequestParam(name = "vaptcha_token",required = false) String vaptcha_token,
                                HttpServletRequest request) {
        if (vaptcha_token != null){
            try {
                if (!vaptchaCheck(vaptcha_token, request.getRemoteHost())){
                    return error("人机验证不通过");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            User sysUser = ShiroUtils.getSysUser();
            // 将未读消息数量放入User
            Message message = new Message();
            message.setSee(0);
            message.setUserId(sysUser.getUserId());
            sysUser.setMessageCount(messageService.selectMessageList(message).size());
            // 将VIP时间格式化
            try {
                sysUser.setVipTimeStr(DateUtils.formatDate(sysUser.getVipTime()));
            }catch (Exception e){
                sysUser.setVipTimeStr(null);
            }
            sysUser.setDept(deptService.selectDeptById(sysUser.getDeptId()));
            request.getSession().setAttribute("currentUser", sysUser);
            return success();
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    @GetMapping("/unauth")
    public String unauth()
    {
        return "error/unauth";
    }

    /**
     * 人机验证结果判断
     *
     * @param token
     * @param ip
     * @return
     * @throws Exception
     */
    private boolean vaptchaCheck(String token, String ip) throws Exception {
        String body = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://api.vaptcha.com/v2/validate");
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("id", "5d8474d0fc650e5af4ec42f7"));
        nvps.add(new BasicNameValuePair("secretkey", "b55e0e1e018f4d148221d296dcffce89"));
        nvps.add(new BasicNameValuePair("scene", ""));
        nvps.add(new BasicNameValuePair("token", token));
        nvps.add(new BasicNameValuePair("ip", ip));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        CloseableHttpResponse r = httpClient.execute(httpPost);
        HttpEntity entity = r.getEntity();

        if (entity != null) {
            body = EntityUtils.toString(entity, "utf-8");
            System.out.println(body);
        }
        r.close();
        httpClient.close();
        Gson gson = new Gson();
        VaptchaMessage message = gson.fromJson(body, VaptchaMessage.class);
        if (message.getSuccess() == 1) {
            return true;
        } else {
            return false;
        }
    }
}
