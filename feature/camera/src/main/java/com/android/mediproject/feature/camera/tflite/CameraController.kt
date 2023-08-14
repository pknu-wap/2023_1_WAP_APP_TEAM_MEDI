package com.android.mediproject.feature.camera.tflite

import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner


/**
 * 카메라를 제어하는 인터페이스
 */
interface CameraController {
    suspend fun connectCamera(previewView: PreviewView): Result<Unit>


    var detectionCallback: CameraHelper.ObjDetectionCallback

    var fragmentLifeCycleOwner: LifecycleOwner
}
