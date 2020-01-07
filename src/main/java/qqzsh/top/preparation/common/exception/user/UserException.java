package qqzsh.top.preparation.common.exception.user;

import qqzsh.top.preparation.common.exception.base.BaseException;
import qqzsh.top.preparation.common.exception.base.BaseException;

/**
 * 用户信息异常类
 * 
 * @author zsh
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }

}
