package com.android.mediproject.core.domain

import androidx.paging.PagingData
import androidx.paging.flatMap
import com.android.mediproject.core.data.remote.comments.CommentsRepository
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.model.comments.toDto
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.LikeCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentsUseCase @Inject constructor(
    private val commentsRepository: CommentsRepository,
) {

    val scrollChannel = Channel<Unit>(capacity = 5, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * 약에 대한 댓글을 가져오는 메서드입니다.
     *
     * @param medicineId 약의 고유 번호
     */
    fun getCommentsForAMedicine(medicineId: Long, myUserId: Long): Flow<PagingData<CommentDto>> = channelFlow {
        commentsRepository.getCommentsForAMedicine(medicineId).collectLatest { pagingData ->
            val result = pagingData.flatMap {
                (it.replies.map { reply ->
                    reply.toDto().apply {
                        reply.likeList.forEach { like ->
                            if (like.userId == myUserId) this.isLiked = true
                        }
                    }
                }.toList().reversed()) + listOf(it.toDto().apply {
                    it.likeList.forEach { like ->
                        if (like.userId == myUserId) this.isLiked = true
                    }
                })
            }
            send(result)
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
            result.fold(onSuccess = {
                Result.success(Unit)
            }, onFailure = { Result.failure(it) })
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