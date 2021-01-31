package com.github.sparkzxl.authentication.infrastructure.repository;


import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.sparkzxl.authentication.infrastructure.entity.CoreOrg;
import com.github.sparkzxl.authentication.infrastructure.entity.CoreStation;
import com.github.sparkzxl.authentication.infrastructure.mapper.CoreStationMapper;
import com.github.sparkzxl.authentication.domain.repository.ICoreStationRepository;
import com.github.sparkzxl.core.utils.MapHelper;
import com.github.sparkzxl.database.annonation.InjectionResult;
import com.github.sparkzxl.database.entity.RemoteData;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description: 岗位 仓储层实现类
 *
 * @author zhouxinlei
 * @date 2020-06-07 13:32:55
 */
@AllArgsConstructor
@Repository
public class CoreStationRepository implements ICoreStationRepository {

    private final CoreStationMapper coreStationMapper;

    @Override
    public Map<Serializable, Object> findStationByIds(Set<Serializable> ids) {
        List<CoreStation> stations = getStations(ids);
        return MapHelper.uniqueIndex(stations, CoreStation::getId, (station) -> station);
    }


    private List<CoreStation> getStations(Set<Serializable> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> idList = ids.stream().mapToLong(Convert::toLong).boxed().collect(Collectors.toList());

        List<CoreStation> list;
        int size = 1000;
        if (idList.size() <= size) {
            list = idList.stream().map(this.coreStationMapper::selectById).filter(Objects::nonNull).collect(Collectors.toList());
        } else {
            list = this.coreStationMapper.selectList(new QueryWrapper<CoreStation>().lambda().in(CoreStation::getId, idList).eq(CoreStation::getStatus, true));
        }
        return list;
    }

    @Override
    @InjectionResult
    public List<CoreStation> getStationPageList(int pageNum, int pageSize, String name, RemoteData<Long, CoreOrg> org) {
        LambdaQueryWrapper<CoreStation> stationQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(name)){
            stationQueryWrapper.likeRight(CoreStation::getName, name);
        }
        if (ObjectUtils.isNotEmpty(org)&&ObjectUtils.isNotEmpty(org.getKey())){
            stationQueryWrapper.eq(CoreStation::getOrg, org);
        }
        PageHelper.startPage(pageNum, pageSize);
        return coreStationMapper.selectList(stationQueryWrapper);
    }
}
