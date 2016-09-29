/*
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package f.com.panoramics.amazon.upload;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 
 * @author Fatty
 *
 */
public class TransferController {
    public static void abort(Context context, TransferModel model) {
        Intent intent = makeIdIntent(context, model.getId());
        intent.setAction(UploadService.ACTION_ABORT);
        context.startService(intent);
    }

    /**
     * 上传Upload
     * 
     * @param context
     * @param uri
     */
    public static void upload(Context context, Uri uri) {
    	System.err.println("start uploadService....");
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(Intent.ACTION_SEND);
        intent.setData(uri);
        context.startService(intent);
    }

    /**
     * 
     * 
     * @param context
     * @param model
     */
    public static void pause(Context context, TransferModel model) {
        Intent intent = makeIdIntent(context, model.getId());
        intent.setAction(UploadService.ACTION_PAUSE);
        context.startService(intent);
    }

    /**
     * 
     * @param context
     * @param model
     */
    public static void resume(Context context, TransferModel model) {
        Intent intent = makeIdIntent(context, model.getId());
        intent.setAction(UploadService.ACTION_RESUME);
        context.startService(intent);
    }

    /**
     * 
     * @param context
     * @param id
     * @return
     */
    private static Intent makeIdIntent(Context context, int id) {
        Intent intent = new Intent(context, UploadService.class);
        intent.putExtra(UploadService.NOTIF_ID_EXTRA, id);
        return intent;
    }
}
