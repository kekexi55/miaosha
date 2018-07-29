package com.imooc.miaosha.exception;

import com.imooc.miaosha.result.CodeMsg;

public class GlobalException extends RuntimeException {
    public CodeMsg cm;
    public GlobalException(CodeMsg cm) {
        super();
        this.cm = cm;
    }
}
