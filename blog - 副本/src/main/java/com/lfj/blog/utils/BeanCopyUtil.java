package com.lfj.blog.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于dto/vo到entity的快速复制工具
 *
 * @Author: LFJ
 * @Date: 2024-03-24 17:03
 */
public class BeanCopyUtil {
	/**
	 * 对象拷贝
	 * 例子: AccessTokenDTO accessTokenDTO = BeanCopyUtil.copyObject(authenticationToken, AccessTokenDTO.class);
	 *
	 * @param source
	 * @param target
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	public static <T> T copyObject(Object source, Class<T> target) {
		T temp = null;
		try {
			temp = target.newInstance();
			if (null != source) {
				org.springframework.beans.BeanUtils.copyProperties(source, temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * 拷贝集合
	 * 例子: List<TreeVo> treeNodes = copyList(personList, TreeVo.class);
	 *
	 * @param source
	 * @param target
	 * @param <T>
	 * @param <S>
	 * @return
	 */
	public static <T, S> List<T> copyList(List<S> source, Class<T> target) {
		List<T> list = new ArrayList<>();
		if (null != source && source.size() > 0) {
			for (Object obj : source) {
				list.add(BeanCopyUtil.copyObject(obj, target));
			}
		}
		return list;
	}

}
