package com.android.pixelated.ui;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.test.suitebuilder.annotation.LargeTest;

import com.android.pixelated.compat.LauncherActivityInfoCompat;
import com.android.pixelated.compat.LauncherAppsCompat;
import com.android.pixelated.compat.UserHandleCompat;
import com.android.pixelated.util.Condition;
import com.android.pixelated.util.Wait;

/**
 * Test for dragging an icon from all-apps to homescreen.
 */
@LargeTest
public class AllAppsIconToHomeTest extends LauncherInstrumentationTestCase {

    private LauncherActivityInfoCompat mSettingsApp;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mSettingsApp = LauncherAppsCompat.getInstance(mTargetContext)
                .getActivityList("com.android.settings", UserHandleCompat.myUserHandle()).get(0);
    }

    public void testDragIcon_portrait() throws Throwable {
        lockRotation(true);
        performTest();
    }

    public void testDragIcon_landscape() throws Throwable {
        lockRotation(false);
        performTest();
    }

    private void performTest() throws Throwable {
        clearHomescreen();
        startLauncher();

        // Open all apps and wait for load complete.
        final UiObject2 appsContainer = openAllApps();
        assertTrue(Wait.atMost(Condition.minChildCount(appsContainer, 2), DEFAULT_UI_TIMEOUT));

        // Drag icon to homescreen.
        UiObject2 icon = scrollAndFind(appsContainer, By.text(mSettingsApp.getLabel().toString()));
        dragToWorkspace(icon);

        // Verify that the icon works on homescreen.
        mDevice.findObject(By.text(mSettingsApp.getLabel().toString())).click();
        assertTrue(mDevice.wait(Until.hasObject(By.pkg(
                mSettingsApp.getComponentName().getPackageName()).depth(0)), DEFAULT_UI_TIMEOUT));
    }
}
