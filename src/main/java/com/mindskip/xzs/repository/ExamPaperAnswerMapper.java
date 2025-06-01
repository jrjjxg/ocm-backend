package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.ExamPaperAnswer;
import com.mindskip.xzs.domain.other.KeyValue;
import com.mindskip.xzs.viewmodel.student.exampaper.ExamPaperAnswerPageVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ExamPaperAnswerMapper extends BaseMapper<ExamPaperAnswer> {

    List<ExamPaperAnswer> studentPage(ExamPaperAnswerPageVM requestVM);

    Integer selectAllCount();

    List<KeyValue> selectCountByDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    ExamPaperAnswer getByPidUid(@Param("pid") Integer paperId, @Param("uid") Integer uid);

    List<ExamPaperAnswer> adminPage(com.mindskip.xzs.viewmodel.admin.paper.ExamPaperAnswerPageRequestVM requestVM);

    /**
     * 根据试卷ID查询答卷
     *
     * @param examPaperId 试卷ID
     * @return 答卷列表
     */
    List<ExamPaperAnswer> selectByExamPaperId(@Param("examPaperId") Integer examPaperId);
    
    /**
     * 根据课程ID和测验ID查询答卷
     *
     * @param courseId 课程ID
     * @param examId 测验ID
     * @return 答卷列表
     */
    List<ExamPaperAnswer> selectByCourseIdAndExamId(@Param("courseId") Long courseId, @Param("examId") Integer examId);
    
    /**
     * 统计测验的答卷数量
     *
     * @param examId 测验ID
     * @return 答卷数量
     */
    Integer countByExamId(@Param("examId") Integer examId);
    
    /**
     * 根据条件查询测验答卷
     *
     * @param examId 测验ID
     * @param studentName 学生姓名
     * @param scoreFrom 最低分
     * @param scoreTo 最高分
     * @return 答卷列表
     */
    List<ExamPaperAnswer> selectByExamIdAndConditions(@Param("examId") Integer examId, 
                                                     @Param("studentName") String studentName, 
                                                     @Param("scoreFrom") Integer scoreFrom, 
                                                     @Param("scoreTo") Integer scoreTo);
}
