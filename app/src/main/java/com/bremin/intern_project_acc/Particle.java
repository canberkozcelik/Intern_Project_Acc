package com.bremin.intern_project_acc;

/**
 * Created by Canberk on 24/06/2015.
 */

public class Particle {
    private static final float COR = 0.5f;

    public float mPosX;
    public float mPosY;
    private float mVelX;
    private float mVelY;

    public void updatePosition(float sx, float sy, float sz, long timestamp) {
        float dt = (System.nanoTime() - timestamp) / 5000000000000.0f;
        mVelX += -sx * dt;
        mVelY += -sy * dt;

        mPosX += mVelX * dt;
        mPosY += mVelY * dt;
    }

    public boolean resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound) {
        if (mPosX > mHorizontalBound) {
            mPosX = mHorizontalBound;
            mVelX = -mVelX * COR;
            return true;
        } else if (mPosX < -mHorizontalBound) {
            mPosX = -mHorizontalBound;
            mVelX = -mVelX * COR;
            return true;
        }
        if (mPosY > mVerticalBound) {
            mPosY = mVerticalBound;
            mVelY = -mVelY * COR;
            return true;
        } else if (mPosY < -mVerticalBound) {
            mPosY = -mVerticalBound;
            mVelY = -mVelY * COR;
            return true;
        }
        return false;
    }

    public float getmPosX() {
        return mPosX;
    }

    public void setmPosX(float mPosX) {
        this.mPosX = mPosX;
    }

    public float getmPosY() {
        return mPosY;
    }

    public void setmPosY(float mPosY) {
        this.mPosY = mPosY;
    }

    public float getmVelX() {
        return mVelX;
    }

    public void setmVelX(float mVelX) {
        this.mVelX = mVelX;
    }

    public float getmVelY() {
        return mVelY;
    }

    public void setmVelY(float mVelY) {
        this.mVelY = mVelY;
    }


}
