package com.hug.single;

import java.io.*;

public class StrBaseHandler {

    public static void main(String[] args) {

//        (`flow_code`, `crf_uid`,  `request_ref_no`,`biz_id`,`withhold_channel`, `trade_amount`, `biz_origin`,   `biz_type_desc`,`notify_url`,  `flow_state`, `request_body`, `create_time`, `modify_time`)
//        VALUES
//                ('c20190329004', 'f17d54b71d8e6f6327fd9d1666356a7b', 'r20190329004','190103000076', '0', '0', '1',  '系统处理,减免结清', '', '1', '',now(), now());
        String filePath = "E:\\str2.txt";
        Long begin = 201903290000L;
        // flowCode crfUid request_ref_no bizId
        String sql = "INSERT INTO crf_withhold_flow" +
                " (`flow_code`, `crf_uid`,  `request_ref_no`,`biz_id`,`withhold_channel`, `trade_amount`, `biz_origin`,   `biz_type_desc`,`notify_url`,  `flow_state`, `request_body`, `create_time`, `modify_time`) " +
                " VALUES " + "('%s', '%s', '%s','%s', '0', '0', '1',  '系统处理,减免结清', '', '1', '',now(), now());";

        int count = 0;
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    // 255821617939905|d7a81eea91f86faf066408fbec3c1628|30000|0
                    String[] str = lineTxt.split("\\|");
                    // bizid crfuid goodsamount settles
//                    258774738477423|3fc75708accdf3fbb228dc26e3d32929|50000|0
//                    258774791555147|9b34d68186f6eb0bd523fd0baa962528|50000|0
//                    258774815527239|ece92d21bf0c72d9889963f4447ec06b|50000|0
                            //
                    count ++;
                    begin = begin + 1;
                    String flowCode = "c" + (begin);
                    String crfUid = str[1];
                    String request_ref_no = "r" + (begin);
                    String bizId = str[0];

                    System.out.println(String.format(sql, flowCode, crfUid, request_ref_no, bizId));

                }
                br.close();
            } else {
                System.out.println("文件不存在!");
            }
            System.out.println("count = "+count);
        } catch (Exception e) {
            System.out.println("文件读取错误!");
        }

        // r20190329001
//        r20190330340
    }
}
