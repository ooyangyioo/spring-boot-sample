package org.yangyi.project.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.mapstruct.DataFormatConverter;
import org.yangyi.project.system.dto.DataFormatDTO;
import org.yangyi.project.system.vo.DataFormatVO;

@RestController
@RequestMapping("/format")
public class DataFormatController {

    @Autowired
    private DataFormatConverter dataFormatConverter;

    @GetMapping(value = "/xml", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public DataFormatVO xml(@RequestBody DataFormatDTO dataFormatDTO) {
        DataFormatVO dataFormatVO = new DataFormatVO();
        dataFormatVO.setName(dataFormatVO.getName());
        dataFormatVO.setPhone("11111111111");
        return dataFormatVO;
    }

    @GetMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataFormatVO json(@RequestBody DataFormatDTO dataFormatDTO) {
        DataFormatVO dataFormatVO = new DataFormatVO();
        dataFormatVO.setName(dataFormatVO.getName());
        dataFormatVO.setPhone("11111111111");
        return dataFormatVO;
    }

}
