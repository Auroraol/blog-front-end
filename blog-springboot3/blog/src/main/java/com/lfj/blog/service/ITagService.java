package com.lfj.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfj.blog.entity.Tag;

import java.util.List;

/**
 * @author 16658
 * @description 针对表【tag(标签表)】的数据库操作Service
 * @createDate 2024-04-04 10:38:49
 */
public interface ITagService extends IService<Tag> {
	/**
	 * 新增标签
	 *
	 * @param tagName
	 * @return void
	 */
	void addTag(String tagName);


	/**
	 * 查询标签id
	 *
	 * @param name
	 * @return
	 */
	int selectIdByName(String name);

	/**
	 * 分页查询标签
	 *
	 * @param current 当前页
	 * @param size    每页数量
	 * @param tagName 标签名模糊查询
	 * @return com.baomidou.mybatisplus.core.metadata.IPage<com.lfj.blog.entity, Tag>
	 */
	IPage<Tag> selectTagPage(long current, long size, String tagName);

	/**
	 * 标签列表
	 *
	 * @param tagName
	 * @return java.util.List<com.lfj.blog.entity.Tag>
	 */
	List<Tag> selectTagList(String tagName);


	/**
	 * 修改标签
	 *
	 * @param id
	 * @param tagName
	 */
	void update(int id, String tagName);

	/**
	 * 删除标签
	 *
	 * @param id
	 */
	void delete(int id);
}
