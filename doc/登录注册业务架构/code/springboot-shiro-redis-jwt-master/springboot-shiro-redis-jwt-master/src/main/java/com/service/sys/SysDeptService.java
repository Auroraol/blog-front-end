package com.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.StringUtils;
import com.common.vo.TreeVo;
import com.entity.sys.SysDept;
import com.mapper.sys.SysDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SysDeptService extends ServiceImpl<SysDeptMapper, SysDept> {

    @Autowired
    private SysDeptMapper deptMapper;

    /**
     * 列表
     */
    public List<SysDept> list(SysDept dept){
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",dept.getStatus())
                    .eq("dept_type",dept.getDeptType());

        if(StringUtils.isNotBlank(dept.getDeptName()))
            queryWrapper.like("dept_name",dept.getDeptName());
        if(dept.getParentId() != null)
            queryWrapper.apply("(dept_id ="+dept.getParentId()+" or parent_id="+dept.getParentId()+")");
        return deptMapper.selectList(queryWrapper);
    }

    /**
     * 构建前端所需树结构
     */
    public List<TreeVo> buildDeptTreeSelect(List<SysDept> deptList) {
        List<SysDept> deptTrees = buildTree(deptList);
        return deptTrees.stream().map(TreeVo::new).collect(Collectors.toList());
    }

    /**
     * 递归列表
     */
    private List<SysDept> buildTree(List<SysDept> list) {
        //按指定节点过滤数据
        List<SysDept> master = list.stream().filter(dept -> dept.getParentId().intValue() == 0).collect(Collectors.toList());
        //将过滤出的节点从数据中移除
        list.removeAll(master);
        //构建分支节点
        List<SysDept> branch = new ArrayList<>();
        master.forEach(dept -> {
            //递归获取分支节点
            dept.setChildren(buildTree(list));
            //将分支节点加入父节点中
            branch.add(dept);
        });
        return branch;
    }

    /**
     * 部门列表
     */
    public List<SysDept> deptList(SysDept dept){
        return deptMapper.deptList(dept);
    }
}
