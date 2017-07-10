package com.douliu.italker.frags.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.douliu.italker.App;
import com.douliu.italker.R;
import com.douliu.italker.activities.MainActivity;
import com.douliu.italker.frags.media.GalleryFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 权限申请界面
 */
public class PermissionsFragment extends BottomSheetDialogFragment
        implements EasyPermissions.PermissionCallbacks {

    private static final int RC = 0x0100;

    private static String[] allPerms = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public PermissionsFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_permissions, container, false);
        root.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshState(getView());
    }

    /**
     * 刷新权限状态
     *
     * @param root
     */
    private void refreshState(View root) {
        if (root == null) {
            return;
        }
        root.findViewById(R.id.im_state_perm_network)
                .setVisibility(hasNetworkPerm(getContext()) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_perm_read)
                .setVisibility(hasReadStoragePerm(getContext()) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_perm_write)
                .setVisibility(hasWriteStoragePerm(getContext()) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_perm_record_audio)
                .setVisibility(hasRecordAudioPerm(getContext()) ? View.VISIBLE : View.GONE);
    }


    /**
     * 是否有网络权限
     *
     * @param context
     * @return
     */
    public static boolean hasNetworkPerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }


    /**
     * 是否有读外部存储的权限
     *
     * @param context
     * @return
     */
    public static boolean hasReadStoragePerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }


    /**
     * 是否有写外部存储的权限
     *
     * @param context
     * @return
     */
    public static boolean hasWriteStoragePerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }


    /**
     * 是否有访问麦克风权限
     *
     * @param context
     * @return
     */
    public static boolean hasRecordAudioPerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 私有的访问方式
     *
     * @param manager
     */
    private static void show(FragmentManager manager) {
        new PermissionsFragment()
                .show(manager, PermissionsFragment.class.getName());
    }

    /**
     * 判断是否有所有的权限
     *
     * @param context 上下文
     * @param manager FragmentManager
     * @return 是否有所有的权限
     */
    public static boolean hasAllPerm(Context context, FragmentManager manager) {
        boolean hasAll = EasyPermissions.hasPermissions(context, allPerms);
        if (!hasAll) {
            show(manager);
        }
        return hasAll;
    }


    /**
     * 发起权限申请
     */
    @AfterPermissionGranted(RC)
    public void requestPermissions() {
        if (!EasyPermissions.hasPermissions(getContext(), allPerms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_ask_again), RC, allPerms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        App.showToast(R.string.label_permission_ok);
        refreshState(getView());
//        dismiss();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
