package com.github.sparkzxl.authentication.interfaces.controller.common;


import com.github.sparkzxl.authentication.interfaces.dto.dictionary.DictionaryPageDTO;
import com.github.sparkzxl.authentication.interfaces.dto.dictionary.DictionarySaveDTO;
import com.github.sparkzxl.authentication.interfaces.dto.dictionary.DictionaryUpdateDTO;
import com.github.sparkzxl.database.base.controller.SuperCacheController;
import com.github.sparkzxl.database.dto.PageParams;
import com.github.sparkzxl.log.annotation.WebLog;
import com.github.sparkzxl.authentication.application.service.ICommonDictionaryService;
import com.github.sparkzxl.authentication.infrastructure.entity.CommonDictionary;
import com.github.sparkzxl.web.annotation.ResponseResult;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 字典类型管理
 *
 * @author: zhouxinlei
 * @date: 2020-07-28 19:48:00
 */
@RestController
@ResponseResult
@WebLog
@Api(tags = "字典类型管理")
@RequestMapping("/common/dictionary")
public class DictionaryController extends SuperCacheController<ICommonDictionaryService, Long,
        CommonDictionary, DictionaryPageDTO, DictionarySaveDTO, DictionaryUpdateDTO> {

    @Override
    public void handlerQueryParams(PageParams<DictionaryPageDTO> params) {
        DictionaryPageDTO paramsModel = params.getModel();
        if (StringUtils.isEmpty(paramsModel.getName())){
            paramsModel.setName(null);
        }
        if (StringUtils.isEmpty(paramsModel.getType())){
            paramsModel.setType(null);
        }
    }

    @Override
    public boolean handlerSave(DictionarySaveDTO model) {
        String type = StringUtils.toRootUpperCase(model.getType());
        model.setType(type);
        return true;
    }

    @Override
    public boolean handlerUpdate(DictionaryUpdateDTO model) {
        String type = StringUtils.toRootUpperCase(model.getType());
        model.setType(type);
        return true;
    }
}
