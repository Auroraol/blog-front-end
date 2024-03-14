package com.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.vo.TreeVo;
import com.entity.sys.SysMenu;
import com.entity.sys.SysRoleMenu;
import com.mapper.sys.SysMenuMapper;
import com.mapper.sys.SysRoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu> {

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    /**
     * 根据角色获取菜单列表
     */
    public List<SysMenu> menuListByRole(String roles){
        QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("FIND_IN_SET(role_id,'"+roles+"')");
        queryWrapper.select("GROUP_CONCAT(menu_id)");
        List<Object> obj = roleMenuMapper.selectObjs(queryWrapper);
        String menus = obj.get(0).toString();
        QueryWrapper<SysMenu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.apply("FIND_IN_SET(id,'"+menus+"')");
        menuQueryWrapper.select("menu_id");
        return baseMapper.selectList(menuQueryWrapper);
    }

    /**
     * 构建前端所需树结构
     */
    public List<TreeVo> buildMenuTreeSelect(List<SysMenu> list) {
        List<SysMenu> trees = buildTree(list);
        return trees.stream().map(TreeVo::new).collect(Collectors.toList());
    }

    /**
     * 递归列表
     */
    private List<SysMenu> buildTree(List<SysMenu> list) {
        //按指定节点过滤数据
        List<SysMenu> master = list.stream().filter(menu -> menu.getParentId().intValue() == 0).collect(Collectors.toList());
        //将过滤出的节点从数据中移除
        list.removeAll(master);
        //构建分支节点
        List<SysMenu> branch = new ArrayList<>();
        master.forEach(menu -> {
            //递归获取分支节点
            menu.setChildren(buildTree(list));
            //将分支节点加入父节点中
            branch.add(menu);
        });
        return branch;
    }

}
