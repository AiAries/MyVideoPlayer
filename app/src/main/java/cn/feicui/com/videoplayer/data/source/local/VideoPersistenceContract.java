/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.feicui.com.videoplayer.data.source.local;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the tasks locally.
 */
public final class VideoPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private VideoPersistenceContract() {}

    /* Inner class that defines the table contents*/
    public static abstract class ViedeoInfoEntry implements BaseColumns {
        public static final String TABLE_NAME = "videoInfo";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_COVER_PIC = "cover_pic";
        public static final String COLUMN_NAME_VIDEO_URL = "url";
        public static final String COLUMN_NAME_AVATAR = "avatar";
        public static final String COLUMN_NAME_CAPTION = "caption";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_VIDEO_TYPE = "type";
    }
}
