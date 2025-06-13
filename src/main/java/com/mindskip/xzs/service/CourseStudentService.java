package com.mindskip.xzs.service;

import com.mindskip.xzs.domain.CourseStudent;
import com.mindskip.xzs.domain.dto.CourseStudentDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 课程-学生关联服务接口
 */
public interface CourseStudentService extends BaseService<CourseStudent> {

    /**
     * 获取课程学生列表
     *
     * @param courseId 课程ID
     * @return 学生列表
     */
    List<CourseStudentDTO> getCourseStudents(Long courseId);

    /**
     * 获取课程学生列表（分页）
     *
     * @param courseId  课程ID
     * @param pageIndex 页码
     * @param pageSize  每页大小
     * @param keyword   搜索关键词
     * @return 分页结果
     */
    PageInfo<CourseStudentDTO> getCourseStudentsPage(Long courseId, Integer pageIndex, Integer pageSize,
            String keyword);

    /**
     * 获取课程学生数量
     *
     * @param courseId 课程ID
     * @return 学生数量
     */
    Integer getStudentCountByCourseId(Long courseId);

    /**
     * 添加学生到课程
     *
     * @param courseId  课程ID
     * @param studentId 学生ID
     * @param creatorId 创建者ID
     * @return 是否成功
     */
    Boolean addStudentToCourse(Long courseId, Integer studentId, Integer creatorId);

    /**
     * 从课程中移除学生
     *
     * @param courseId  课程ID
     * @param studentId 学生ID
     * @return 是否成功
     */
    Boolean removeStudentFromCourse(Long courseId, Integer studentId);

    /**
     * 更新学生的课程成绩
     *
     * @param courseId  课程ID
     * @param studentId 学生ID
     * @param score     成绩
     * @return 是否成功
     */
    Boolean updateStudentScore(Long courseId, Integer studentId, Float score);

    /**
     * 根据ID查询课程学生关联
     *
     * @param id 关联ID (Long类型)
     * @return 关联信息
     */
    CourseStudent selectById(Long id);

    /**
     * 根据ID删除课程学生关联
     *
     * @param id 关联ID (Long类型)
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 检查学生是否选修了课程
     *
     * @param courseId  课程ID
     * @param studentId 学生ID
     * @return 是否选修
     */
    Boolean isStudentEnrolled(Long courseId, Integer studentId);
}