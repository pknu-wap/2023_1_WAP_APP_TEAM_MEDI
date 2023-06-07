package com.android.mediproject.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.data.remote.comments.CommentsRepository
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.model.comments.toDto
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.LikeCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class CommentsUseCase @Inject constructor(
    private val commentsRepository: CommentsRepository,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher) {

    /**
     * 약에 대한 댓글을 가져오는 메서드입니다.
     *
     * @param itemSeq 약의 고유 번호
     */
    fun getCommentsForAMedicine(itemSeq: String): Flow<PagingData<CommentDto>> =
        commentsRepository.getCommentsForAMedicine(itemSeq).map {
            it.map { response ->
                response.toDto()
            }
        }

    /**
     * 내가 작성한 댓글을 가져오는 메서드입니다.
     */
    fun getMyComments(userId: Int): Flow<PagingData<MyCommentDto>> {
        TODO()
    }


    /**
     * 댓글을 수정합니다.
     */
    fun applyEditedComment(parameter: EditCommentParameter): Flow<Result<Unit>> =
        commentsRepository.applyEditedComment(parameter).mapLatest { result ->
            result.fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })
        }


    /**
     * 댓글을 등록합니다.
     */
    fun applyNewComment(parameter: NewCommentParameter): Flow<Result<Unit>> =
        commentsRepository.applyNewComment(parameter).mapLatest { result ->
            result.fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })
        }


    /**
     * 댓글 삭제 클릭
     */
    fun deleteComment(parameter: DeleteCommentParameter): Flow<Result<Unit>> =
        commentsRepository.deleteComment(parameter).mapLatest { result ->
            result.fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })
        }


    /**
     * 댓글 좋아요 클릭
     */
    fun likeComment(parameter: LikeCommentParameter): Flow<Result<Unit>> = commentsRepository.likeComment(parameter).mapLatest { result ->
        result.fold(onSuccess = { Result.success(Unit) }, onFailure = { Result.failure(it) })
    }

}