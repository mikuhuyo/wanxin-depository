package com.wanxin.depository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanxin.depository.entity.Project;
import com.wanxin.depository.model.CreateProjectResponse;
import com.wanxin.depository.model.ModifyProjectResponse;

/**
 * <p>
 * 标的信息表 服务类
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
public interface ProjectService extends IService<Project> {

    /**
     * 创建标的
     *
     * @param reqData 业务数据
     * @return
     */
    CreateProjectResponse createProject(String reqData);

    /**
     * 更新标的状态
     *
     * @param reqData 业务数据
     * @return
     */
    ModifyProjectResponse modifyProject(String reqData);

    /**
     * 根据标的编号获取标的信息
     *
     * @param projectNo
     * @return
     */
    Project getByProjectNo(String projectNo);

}
