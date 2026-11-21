package com.ranjan.somiq.core.platform

import platform.UIKit.*
import platform.Foundation.*
import platform.darwin.NSObject
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class IosMediaPicker(
    private val rootViewController: UIViewController
) : MediaPicker {

    private var delegate: PickerDelegate? = null

    override suspend fun pickMedia(type: MediaType): String? = suspendCancellableCoroutine { cont ->
        val picker = UIImagePickerController()
        picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
        
        picker.mediaTypes = when (type) {
            MediaType.IMAGE -> listOf("public.image")
            MediaType.VIDEO -> listOf("public.movie")
        }

        delegate = PickerDelegate(cont) { 
            delegate = null 
        }
        picker.delegate = delegate
        
        rootViewController.presentViewController(picker, animated = true, completion = null)
    }

    private class PickerDelegate(
        private val cont: CancellableContinuation<String?>,
        private val onClear: () -> Unit
    ) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

        override fun imagePickerController(
            picker: UIImagePickerController,
            didFinishPickingMediaWithInfo: Map<Any?, *>
        ) {
            val url = didFinishPickingMediaWithInfo[UIImagePickerControllerMediaURL] as? NSURL
            val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
            
            val resultPath = url?.absoluteString ?: saveImageToTemp(image)
            
            picker.dismissViewControllerAnimated(true) {
                cont.resume(resultPath)
                onClear()
            }
        }

        override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
            picker.dismissViewControllerAnimated(true) {
                cont.resume(null)
                onClear()
            }
        }

        private fun saveImageToTemp(image: UIImage?): String? {
            if (image == null) return null
            val data = UIImageJPEGRepresentation(image, 1.0) ?: return null
            val tempPath = NSTemporaryDirectory() + NSUUID().UUIDString + ".jpg"
            data.writeToFile(tempPath, atomically = true)
            return "file://$tempPath"
        }
    }
}
