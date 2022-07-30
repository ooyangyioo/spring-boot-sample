package org.yangyi.project.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

public class ExcelUtil {

    public static void main(String[] args) throws Exception {
        new ExcelUtil().excelOutput();
    }

    public void excelOutput() throws Exception {
        ExcelModel excelModelY = new ExcelModel();
        excelModelY.setAge(30);
        excelModelY.setName("杨");
        excelModelY.setPhone("155");
        excelModelY.setAddress("襄阳");

        ExcelModel excelModelD = new ExcelModel();
        excelModelD.setAge(18);
        excelModelD.setName("杜");
        excelModelD.setPhone("134");
        excelModelD.setAddress("仙桃");

        List<ExcelModel> list = new ArrayList<>() {{
            add(excelModelD);
            add(excelModelY);
        }};
        Map<String, String> map = new HashMap<>() {{
            put("date", "2022-06-06");
            put("user", "管理员");
        }};

        String outPath = "C:\\Users\\杨毅\\Desktop\\" + UUID.randomUUID() + ".xlsx";
        InputStream excelWriterInputStream = this.getClass().getClassLoader().getResourceAsStream("template.xlsx");
        ExcelWriter excelWriter = EasyExcel.write(new FileOutputStream(outPath)).withTemplate(excelWriterInputStream).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(list, writeSheet);
        excelWriter.fill(map, writeSheet);
        excelWriter.finish();
    }

}
