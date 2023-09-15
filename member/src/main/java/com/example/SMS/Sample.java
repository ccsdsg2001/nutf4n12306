//package com.example.SMS;
//
//import com.aliyun.tea.TeaException;
//
///**
// * @author cc
// * @date 2023年09月15日 14:53
// */
//public class Sample {
//
//    /**
//     * 使用AK&SK初始化账号Client
//     * @param accessKeyId
//     * @param accessKeySecret
//     * @return Client
//     * @throws Exception
//     */
//    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
//        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
//                // 必填，您的 AccessKey ID
//                .setAccessKeyId("LTAI5tCQqE2hF8fane3pDxTe")
//                // 必填，您的 AccessKey Secret
//                .setAccessKeySecret("LTAI5tCQqE2hF8fane3pDxTe");
//        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
//        config.endpoint = "dysmsapi.aliyuncs.com";
//        return new com.aliyun.dysmsapi20170525.Client(config);
//    }
//
//    public static void main(String[] args_) throws Exception {
//        java.util.List<String> args = java.util.Arrays.asList(args_);
//        // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
//        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
//        com.aliyun.dysmsapi20170525.Client client = Sample.createClient(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"), System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"));
//        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
//                .setPhoneNumbers("15521221790")
//                .setSignName("我的商家后台管理系统");
//        try {
//            // 复制代码运行请自行打印 API 的返回值
//            client.sendSmsWithOptions(sendSmsRequest, new com.aliyun.teautil.models.RuntimeOptions());
//        } catch (TeaException error) {
//            // 如有需要，请打印 error
//            com.aliyun.teautil.Common.assertAsString(error.message);
//        } catch (Exception _error) {
//            TeaException error = new TeaException(_error.getMessage(), _error);
//            // 如有需要，请打印 error
//            com.aliyun.teautil.Common.assertAsString(error.message);
//        }
//    }
//}