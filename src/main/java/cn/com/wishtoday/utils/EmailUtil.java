package cn.com.wishtoday.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EmailUtil {
    /**
     * 截取指令流水号，只显示后6位
     */
    public static String setSeqNo(String seqNo){
        String var1 = seqNo.substring(0, seqNo.length()-6);
        String var2 ="****";
        return seqNo.replace(var1 ,var2);
    }

    private final static String htmlHead = "<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body>";
    /**
     * 邮件信息内容
     */
    public static String setTableContent(Map<String, Object> map){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( htmlHead + "<h2>" + map.get("contentHead") + "</h2>");
        stringBuilder.append("<table border=\"5\" style=\"border:solid 1px #E8F2F9;font-size=14px;;font-size:22px;\">" +
                "<tr style=\"background-color: #428BCA; color:#ffffff\">" +
                "<th width=\"200px\">流水号</th>" +
                "<th width=\"200px\">风险点检查项</th>" +
                "<th width=\"200px\">反馈时限</th>" +
                "</tr>" +
                "</tr>" +
                "<td style=\"text-align:center\">" + map.get("reqSn") + "</td>" +
                "<td style=\"text-align:center\">" + map.get("busiCode") + "</td>" +
                "<td style=\"text-align:center\">" + map.get("timeLimit") + "</td>" +
                "</tr>" +
                "</table>" +
                "</body></html>");
        stringBuilder.append(map.get("url"));
        return stringBuilder.toString();
    }

    /**
     * 判断邮箱地址是否正确
     */
    public static boolean emailValidator(String email) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

    public static void main(String[] args) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ld = LocalDate.now();
        Map<String, Object> map = new HashMap<>();
        map.put("contentHead", "收到（信息报送）指令：");
        map.put("reqSn", EmailUtil.setSeqNo("I323243243525554654"));
        map.put("busiCode", "征信信息报送");
        map.put("timeLimit", ld.format(fmt));
    }

    /**
     * 获取数组类型的收件人
     */
    public static String[] getRecipients(String[] recipients) {
        //recipients = new String[]{"yumingxing@dhcc.com.cn", "lixiaofei4531@dhcc.com.cn", "sunshishuai@dhcc.com.cn"};
        //String[]  recipients = new String[]{"yumingxing@dhcc.com.cn"};
        // 过滤空值、邮箱验证并转成新数组
        return Arrays.stream(recipients)
                .filter(s -> s != null && !s.isEmpty() && emailValidator(s))
                .toArray(String[]::new);
    }
}
