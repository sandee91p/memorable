package com.zetaco.entity.am;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.UUID;

import com.zetaco.utils.compat.BuildCompat;

public class PendingResultData implements Parcelable {
    public final int mType;
    public final boolean mOrderedHint;
    public final boolean mInitialStickyHint;
    public final IBinder mToken;
    public final int mSendingUser;
    public int mFlags;
    public int mResultCode;
    public final String mResultData;
    public final Bundle mResultExtras;
    public final boolean mAbortBroadcast;
    public final boolean mFinished;
    public final String mBToken;

    public PendingResultData(BroadcastReceiver.PendingResult pendingResult) {
        this.mBToken = UUID.randomUUID().toString();
        if (BuildCompat.isM()) {
            this.mType = org.android.content.BroadcastReceiver.PendingResultM.mType.get(pendingResult);
            this.mOrderedHint = org.android.content.BroadcastReceiver.PendingResultM.mOrderedHint.get(pendingResult);
            this.mInitialStickyHint = org.android.content.BroadcastReceiver.PendingResultM.mInitialStickyHint.get(pendingResult);
            this.mToken = org.android.content.BroadcastReceiver.PendingResultM.mToken.get(pendingResult);
            this.mSendingUser = org.android.content.BroadcastReceiver.PendingResultM.mSendingUser.get(pendingResult);
            this.mFlags = org.android.content.BroadcastReceiver.PendingResultM.mFlags.get(pendingResult);
            this.mResultData = org.android.content.BroadcastReceiver.PendingResultM.mResultData.get(pendingResult);
            this.mResultExtras = org.android.content.BroadcastReceiver.PendingResultM.mResultExtras.get(pendingResult);
            this.mAbortBroadcast = org.android.content.BroadcastReceiver.PendingResultM.mAbortBroadcast.get(pendingResult);
            this.mFinished = org.android.content.BroadcastReceiver.PendingResultM.mFinished.get(pendingResult);
        } else {
            this.mType = org.android.content.BroadcastReceiver.PendingResult.mType.get(pendingResult);
            this.mOrderedHint = org.android.content.BroadcastReceiver.PendingResult.mOrderedHint.get(pendingResult);
            this.mInitialStickyHint = org.android.content.BroadcastReceiver.PendingResult.mInitialStickyHint.get(pendingResult);
            this.mToken = org.android.content.BroadcastReceiver.PendingResult.mToken.get(pendingResult);
            this.mSendingUser = org.android.content.BroadcastReceiver.PendingResult.mSendingUser.get(pendingResult);
            this.mResultData = org.android.content.BroadcastReceiver.PendingResult.mResultData.get(pendingResult);
            this.mResultExtras = org.android.content.BroadcastReceiver.PendingResult.mResultExtras.get(pendingResult);
            this.mAbortBroadcast = org.android.content.BroadcastReceiver.PendingResult.mAbortBroadcast.get(pendingResult);
            this.mFinished = org.android.content.BroadcastReceiver.PendingResult.mFinished.get(pendingResult);
        }
    }

    public BroadcastReceiver.PendingResult build() {
        if (BuildCompat.isM()) {
            return org.android.content.BroadcastReceiver.PendingResultM._new.newInstance(mResultCode, mResultData, mResultExtras, mType, mOrderedHint, mInitialStickyHint, mToken, mSendingUser, mFlags);
        }
        return org.android.content.BroadcastReceiver.PendingResult._new.newInstance(mResultCode, mResultData, mResultExtras, mType, mOrderedHint, mInitialStickyHint, mToken, mSendingUser);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeByte(this.mOrderedHint ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mInitialStickyHint ? (byte) 1 : (byte) 0);
        dest.writeStrongBinder(this.mToken);
        dest.writeInt(this.mSendingUser);
        dest.writeInt(this.mFlags);
        dest.writeInt(this.mResultCode);
        dest.writeString(this.mResultData);
        dest.writeBundle(this.mResultExtras);
        dest.writeByte(this.mAbortBroadcast ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mFinished ? (byte) 1 : (byte) 0);
        dest.writeString(this.mBToken);
    }

    protected PendingResultData(Parcel in) {
        this.mType = in.readInt();
        this.mOrderedHint = in.readByte() != 0;
        this.mInitialStickyHint = in.readByte() != 0;
        this.mToken = in.readStrongBinder();
        this.mSendingUser = in.readInt();
        this.mFlags = in.readInt();
        this.mResultCode = in.readInt();
        this.mResultData = in.readString();
        this.mResultExtras = in.readBundle();
        this.mAbortBroadcast = in.readByte() != 0;
        this.mFinished = in.readByte() != 0;
        this.mBToken = in.readString();
    }

    public static final Parcelable.Creator<PendingResultData> CREATOR = new Parcelable.Creator<PendingResultData>() {
        @Override
        public PendingResultData createFromParcel(Parcel source) {
            return new PendingResultData(source);
        }

        @Override
        public PendingResultData[] newArray(int size) {
            return new PendingResultData[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "PendingResultData{" + "mType=" + mType + ", mOrderedHint=" + mOrderedHint + ", mInitialStickyHint=" + mInitialStickyHint + ", mToken=" + mToken +
                ", mSendingUser=" + mSendingUser + ", mFlags=" + mFlags + ", mResultCode=" + mResultCode + ", mResultData='" + mResultData + '\'' +
                ", mResultExtras=" + mResultExtras + ", mAbortBroadcast=" + mAbortBroadcast + ", mFinished=" + mFinished + '}';
    }
}
