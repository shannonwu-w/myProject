//package com.myproject.server.domain.dto;
//
//import lombok.Data;
//
//@Data
//public class CertResult {
//    private boolean success;
//    private String message;
//    private Object data; // 這裡之後會用來放你的 UserCert
//
//    public static CertResult success(String msg, Object data) {
//        CertResult r = new CertResult();
//        r.setSuccess(true);
//        r.setMessage(msg);
//        r.setData(data);
//        return r;
//    }
//
//    public static CertResult error(String msg) {
//        CertResult r = new CertResult();
//        r.setSuccess(false);
//        r.setMessage(msg);
//        return r;
//    }
//}