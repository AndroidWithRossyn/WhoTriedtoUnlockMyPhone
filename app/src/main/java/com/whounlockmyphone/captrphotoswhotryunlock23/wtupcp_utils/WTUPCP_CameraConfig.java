package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils;

import android.content.Context;
import android.os.Environment;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.config.WTUPCP_CameraImageFormat;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.config.WTUPCP_CameraResolution;
import java.io.File;

public final class WTUPCP_CameraConfig {
    private Context mContext;
    
    public int mFacing = 0;
    
    public File mImageFile;
    
    public int mImageFormat = WTUPCP_CameraImageFormat.FORMAT_JPEG;
    
    public int mImageRotation = 0;
    
    public Long mImageTime;
    
    public int mResolution = WTUPCP_CameraResolution.MEDIUM_RESOLUTION;

    public Builder getBuilder(Context context) {
        this.mContext = context;
        return new Builder();
    }

    public int getResolution() {
        return this.mResolution;
    }

    public int getFacing() {
        return this.mFacing;
    }

    public int getImageFormat() {
        return this.mImageFormat;
    }

    public File getImageFile() {
        return this.mImageFile;
    }

    public Long getImageTime() {
        return this.mImageTime;
    }

    public int getImageRotation() {
        return this.mImageRotation;
    }

    public class Builder {
        public Builder() {
        }

        public Builder setCameraResolution(int i) {
            if (i == 2006 || i == 7895 || i == 7821) {
                int unused = WTUPCP_CameraConfig.this.mResolution = i;
                return this;
            }
            throw new RuntimeException("Invalid camera resolution.");
        }

        public Builder setCameraFacing(int i) {
            if (i == 0 || i == 1) {
                int unused = WTUPCP_CameraConfig.this.mFacing = i;
                return this;
            }
            throw new RuntimeException("Invalid camera facing value.");
        }

        public Builder setImageFormat(int i) {
            if (i == 849 || i == 545) {
                int unused = WTUPCP_CameraConfig.this.mImageFormat = i;
                return this;
            }
            throw new RuntimeException("Invalid output image format.");
        }

        public Builder setImageRotation(int i) {
            if (i == 0 || i == 90 || i == 180 || i == 270) {
                int unused = WTUPCP_CameraConfig.this.mImageRotation = i;
                return this;
            }
            throw new RuntimeException("Invalid image rotation.");
        }

        public Builder setImageFile(File file) {
            File unused = WTUPCP_CameraConfig.this.mImageFile = file;
            return this;
        }

        public WTUPCP_CameraConfig build() {
            if (WTUPCP_CameraConfig.this.mImageTime == null) {
                Long unused = WTUPCP_CameraConfig.this.mImageTime = Long.valueOf(System.currentTimeMillis());
            }
            if (WTUPCP_CameraConfig.this.mImageFile == null) {
                File unused2 = WTUPCP_CameraConfig.this.mImageFile = getDefaultStorageFile();
            }
            return WTUPCP_CameraConfig.this;
        }

        private File getDefaultStorageFile() {
            String str = Environment.getExternalStorageDirectory() + "/Who Tried to unlock my Phone? Capture Photos";
            if (!new File(str).exists()) {
                new File(str).mkdirs();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(System.currentTimeMillis());
            sb.append(WTUPCP_CameraConfig.this.mImageFormat == 849 ? ".jpg" : ".png");
            return new File(str, sb.toString());
        }
    }
}
