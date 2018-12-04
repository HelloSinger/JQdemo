package com.jq.btlib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.jq.btlib.model.BtGattAttr;
import com.jq.btlib.model.DataType;
import com.jq.btlib.model.Frame;
import com.jq.btlib.model.FrameData;
import com.jq.btlib.model.device.CsDevice;
import com.jq.btlib.model.device.CsFatScale;
import com.jq.btlib.model.device.CsWeigher;
import com.jq.btlib.model.exception.FrameFormatIllegalException;

/**
 * 蓝牙工具包，芯海蓝牙芯片的控制集合
 * */

public class CsBtUtil {

	// =====================================定义常量=======================================
	// 打印相关的常变量
	/** 标识 **/
	private static final String TAG = "CsBtUtil";

	/** 表中元素列表为公司蓝牙芯片显示的名字 **/
	public interface CHIPSEA {
		/** 蓝牙核心名字 **/
        String BT_NAME = "Chipsea-BLE";
	}

	/** uuid值-心跳检测 **/
	public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID
			.fromString(BtGattAttr.HEART_RATE_MEASUREMENT);

	/** uuid值-输入 **/
	public final static UUID UUID_ISSC_RX = UUID
			.fromString(BtGattAttr.ISSC_CHAR_RX_UUID);

	/** 连接成功标准 **/
	public final static int ACTION_GATT_CONNECTED = 200;
	/** 尝试连接标志 **/
	public final static int ACTION_GATT_TRY_CONNECT = 201;
	/** 断开连接状态 **/
	public final static int ACTION_GATT_DISCONNECTED = -1;
	
	public final static int STATE_OPENED = 1; //打开未连接
	public final static int STATE_CLOSE = 2; //关闭
	public final static int STATE_BROADCAST = 3; //接受广播中
	public final static int STATE_CONNECTING = 4;//正在连接
	public final static int STATE_WRITTING = 5;// 连接上了，正在下传数据
	public final static int STATE_READING = 6; // 秤获得数据后，上传数据，并且正在读数据

	/** 是否允许广播,当为false的时候,不能执行广播语句 **/
	private static boolean isAllowedBroadcast = false;
	
		// =====================================定义变量=======================================
	private BluetoothAdapter mBtAdapter = null; // 蓝牙的适配器
	private BluetoothGatt mBtGatt; // 本手机蓝牙作为中央来使用和处理数据
	private BluetoothGattCharacteristic mWriteCharacteristic; // 用来保存写入到设备数据！
	private BluetoothGattCharacteristic mNotifyCharacteristic; // 用来通知，有数据改变~！
	private Context context;
	private static OnBluetoothListener bluetoothListener = null;

	private int currentConnectState = ACTION_GATT_DISCONNECTED;
	// ========凡是透传的设备都需要在这里定义========

	private CsWeigher weigher = null;
	private CsFatScale fatScale = null;

	/**
	 * Device scan callback. 通过回调来显示是否搜索到 设备和接收到设备的广播,在这里处理以下事件 (1)
	 * 保存ListDevices列表 (2) 解包广播包数据
	 **/
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
				final byte[] scanRecord) {
			if (device == null) {
				return;
			}
			LogUtil.i("blelockcheck", isAllowedBroadcast+"");
			// 只有被允许了广播才会执行以下的语句
			if (isAllowedBroadcast) {
				ThreadUtil.executeThread(new Runnable() {
					@Override
					public void run() {
						// 1. ============以下 是用来蓝牙设备的名字和地址=================
						synchronized (this) {
							handleBroadcastInfo(device,rssi,scanRecord);
						}// end synchronized
					}
				});

			}// end onLeScan
		}
	};

	/**
	 * 连接成功后，数据回调的变量 里面回调函数有 ： 1. 数据改变回调函数 2. 数据连接状态的回调函数 3. 设备发现回调函数 4.
	 * 读设备函数回调函数 5. 写设备函数回调函数 6. 读取信号回调函数
	 * */
	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			
			super.onConnectionStateChange(gatt, status, newState);
			LogUtil.i(TAG, " onConnectionStateChange ");
			if (newState == BluetoothProfile.STATE_CONNECTED) { // 连接状态
				LogUtil.i(TAG, "Connected to GATT server. ");
				// Attempts to discover services after successful connection.
				boolean result = mBtGatt.discoverServices(); // 尝试去发现设备
				LogUtil.i(TAG, "Attempting to start service discovery:"
						+ result);

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				LogUtil.i(TAG, "Disconnected from GATT server.");
				currentConnectState = ACTION_GATT_DISCONNECTED;
				if (bluetoothListener != null) {
					bluetoothListener.disconnectBluetooth();
					bluetoothListener.bluetoothStateChange(STATE_OPENED);
				}
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			
			super.onServicesDiscovered(gatt, status);
			LogUtil.i(TAG, "onServicesDiscovered function");

			if (status == BluetoothGatt.GATT_SUCCESS) { // 连接成功
				LogUtil.i(TAG, "connetting device sucess");
				List<BluetoothGattService> mbtgatt = mBtGatt.getServices();
				// 必须在设备被找到后才可以使用getServices！
				displayGattServices(mbtgatt);

			} else {
				LogUtil.i(TAG, "onServicesDiscovered received: " + status);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicRead(gatt, characteristic, status);
			LogUtil.i(TAG, "onCharacteristicRead ");
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicWrite(gatt, characteristic, status);
			LogUtil.i(TAG, " onCharacteristicWrite ");
		}

		// 连接之后,对数据格式的判断和解析
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			super.onCharacteristicChanged(gatt, characteristic);
			//在这里处理所有的
			handleConnectedInfo(characteristic);
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			
			super.onReadRemoteRssi(gatt, rssi, status);
			LogUtil.i(TAG, "onReadRemoteRssi ");
		}

	};

	/**
	 * 析构函数
	 * */
	public CsBtUtil(Context context) {
		this.context = context;
		this.mBtAdapter = BluetoothAdapter.getDefaultAdapter(); // 得到默认的蓝牙适配器

	}

	// ===================================自定义函数===================================
	/**
	 * 设置回调函数
	 * 
	 * @param listener
	 */
	public void setBluetoothListener(OnBluetoothListener listener) {
		CsBtUtil.bluetoothListener = listener;
		CsBtUtil.isAllowedBroadcast = true;
	}

	/**
	 * 打开蓝牙
	 *
	 * @param method
	 *            1 为通知的方式打开蓝牙 2 为静默打开蓝牙
	 * */
	public void openBluetooth(boolean isSilent) {
		if (!mBtAdapter.isEnabled()) {
			// 方法一打开蓝牙 -- 打开的时候会有提示是否打开蓝牙
			if (!isSilent) {
				Intent enabler = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				context.startActivity(enabler);
			} else {
				mBtAdapter.enable();
			}
		}
	}

	/**
	 * 关闭蓝牙
	 * */
	public void closeBluetooth() {
		if (mBtAdapter != null) {
			mBtAdapter.disable();
		}
	}

	/**
	 * 判断蓝牙是否已经使能
	 * */
	public boolean isBluetoothEnable() {
		if (mBtAdapter != null) {
			return mBtAdapter.isEnabled();
		}
		return false;
	}

	/**
	 * 判断当前是否处于连接状态
	 * 
	 * @return
	 */
	public boolean isConnected() {
		if (currentConnectState == ACTION_GATT_CONNECTED) {
			return true;
		} else if (currentConnectState == ACTION_GATT_DISCONNECTED) {
			return false;
		}
		return false;
	}

	/**
	 * 判断当前是否处于连接状态
	 * 
	 * @return
	 */
	public boolean isTryConnected() {
		if (currentConnectState == ACTION_GATT_TRY_CONNECT) {
			return true;
		} else if (currentConnectState == ACTION_GATT_DISCONNECTED) {
			return false;
		}
		return false;
	}

	/**
	 * 搜索获取数据
	 * */
	public boolean startSearching2() {
		boolean isSucess = false;
		if (mBtAdapter != null) {
			if (mBtAdapter.isEnabled()) {
				isSucess = mBtAdapter.startLeScan(mLeScanCallback);
			}
		}
		return isSucess;
	}

	/**
	 * 停止搜索
	 * */
	public void stopSearching2() {
		if (mBtAdapter != null) {
			if (mBtAdapter.isEnabled() && (mLeScanCallback != null)) {
				mBtAdapter.stopLeScan(mLeScanCallback);
			}
		}
	}
	
	/**
	 * 搜索获取数据
	 * */
	public boolean startSearching() {
		boolean isSucess = false;
		isAllowedBroadcast = true;
		if (mBtAdapter != null) {
			if (mBtAdapter.isEnabled()) {
				isSucess = mBtAdapter.startLeScan(mLeScanCallback);
			}
		}
		return isSucess;
	}

	/**
	 * 停止搜索
	 * */
	public void stopSearching() {
		if (mBtAdapter != null) {
			isAllowedBroadcast = false;
			if (mBtAdapter.isEnabled() && (mLeScanCallback != null)) {
				mBtAdapter.stopLeScan(mLeScanCallback);
			}
		}
	}

	// ========================================连接BLE设备函数======================================
	// ========================================连接BLE设备函数======================================
	// ========================================连接BLE设备函数======================================
	/**
	 * 连接函数
	 * */
	public boolean connectBTDevice(final String address) {

		if (mBtAdapter == null || address == null) {
			LogUtil.e(TAG, "connectBTDevice param error");
			return false;
		}
		// 假如已经有地址和 BluetoothGatt不为空的时候，直接进入以下连接状态
		if (address != null && mBtGatt != null) {
			if (mBtGatt.connect()) { // 连接！
				LogUtil.i(TAG, " connectBTDevice sucess  ");
				return true;
			} else {
				return false;
			}
		}

		// 通过地址获得远程设备 device
		final BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
		if (device == null) {
			LogUtil.e(TAG, "Device not found. Unable to connect.");
			return false;
		}

		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		mBtGatt = device.connectGatt(context, false, mGattCallback); // 连接
		if (mBtGatt != null) {
			LogUtil.i(TAG, "! Already got BluetoothGatt !");
			currentConnectState = ACTION_GATT_TRY_CONNECT; // 尝试连接
			if(bluetoothListener != null){
				bluetoothListener.bluetoothStateChange(STATE_CONNECTING);
			}
		}
		return true;
	}

	/**
	 * 关闭连接
	 * */
	public void disconnectBTDevice() {

		if (mBtAdapter == null || mBtGatt == null) {
			return;
		}
		LogUtil.i(TAG, "disconnectBluetoothGatt");
		mBtGatt.disconnect();
	}

	/**
	 * 当完成所有操作之后，一定要调用本函数，释放资源
	 */
	public void closeBluetoothGatt() {
		if (mBtGatt == null) {
			return;
		}
		mBtGatt.close();
		mBtGatt = null;
	}

	/**
	 * 读函数 -- 从设备中读取信息
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {

		if (mBtAdapter == null || mBtGatt == null || characteristic == null) {
			LogUtil.e(TAG, " readCharacteristic : error ");
			return;
		}
		mBtGatt.readCharacteristic(characteristic);
	}

	/**
	 * 写函数，用来把信息写入到远程设备中
	 * */
	public void writeCharacteristic(byte[] value) {
		writeCharacteristic(mWriteCharacteristic, value);
	}

	public void writeCharacteristic(BluetoothGattCharacteristic characteristic,
			byte[] value) {
		if (mBtGatt == null) {
			LogUtil.e(TAG, "mBtGatt error");
			return;
		}
		if (mBtAdapter == null) {
			LogUtil.e(TAG, "mBtAdapter error");
			return;
		}
		if (characteristic == null) {
			LogUtil.e(TAG, "WriteCharacteristic error");
			return;
		}
		// final int charaProp = characteristic.getProperties();
		// if ((charaProp |
		// BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
		// characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
		// } else {
		// characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
		// }
		Log.v(TAG,
				"writeCharacteristic --> " + BytesUtil.bytesToHexString(value));
		characteristic.setValue(value);
		mBtGatt.writeCharacteristic(characteristic);
		if(bluetoothListener != null){
			bluetoothListener.bluetoothStateChange(STATE_WRITTING);
		}
	}

	/**
	 * 就是用来设置Characteristic -- 就是读写节点变量
	 * */
	private void displayGattServices(List<BluetoothGattService> gattServices) {
		if (gattServices == null)
			return;
		String uuid = null;
		String unknownServiceString = "Unknown Devices";
		String unknownCharaString = "Unknown characteristic";
		ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
		ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();

		// Loops through available GATT Services.
		for (BluetoothGattService gattService : gattServices) {

			HashMap<String, String> currentServiceData = new HashMap<String, String>();
			uuid = gattService.getUuid().toString();
			currentServiceData.put("LIST_NAME",
					BtGattAttr.lookup(uuid, unknownServiceString));
			currentServiceData.put("LIST_UUID", uuid);
			gattServiceData.add(currentServiceData);

			ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService
					.getCharacteristics();
			ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

			// Loops through available Characteristics.
			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
				charas.add(gattCharacteristic);
				HashMap<String, String> currentCharaData = new HashMap<String, String>();
				uuid = gattCharacteristic.getUuid().toString();
				currentCharaData.put("LIST_NAME",
						BtGattAttr.lookup(uuid, unknownCharaString));
				currentCharaData.put("LIST_UUID", uuid);
				gattCharacteristicGroupData.add(currentCharaData);

				// ++++++++++++++++++
				final int charaProp = gattCharacteristic.getProperties();

				Log.e("", charaProp + "===UUID:"
						+ gattCharacteristic.getUuid().toString());

				if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {

					// 判断一下UUID
					if (gattCharacteristic.getUuid().toString()
							.compareToIgnoreCase(BtGattAttr.ISSC_CHAR_RX_UUID) == 0) {
						Log.e("", "got NOTIFY characteristic ---- ");
						mNotifyCharacteristic = gattCharacteristic;
						setCharacteristicNotification(mNotifyCharacteristic,
								true);
					}
				}

				if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0
						|| (charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
					// 判断一下UUID
					if (gattCharacteristic.getUuid().toString()
							.compareToIgnoreCase(BtGattAttr.ISSC_CHAR_TX_UUID) == 0) {
						mWriteCharacteristic = gattCharacteristic;
						Log.e("", "got WRITE characteristic !!!!!---- ");
						currentConnectState = ACTION_GATT_CONNECTED;
						if ((bluetoothListener != null)
								&& (mNotifyCharacteristic != null)
								&& (mWriteCharacteristic != null)) {
							bluetoothListener
									.didConnectedGetWriteNotifyCharacteristic(
											mNotifyCharacteristic,
											mWriteCharacteristic);
						}
					}
				}
			}
			gattCharacteristicData.add(gattCharacteristicGroupData);
		}
	}

	/**
	 * 用以设置接受提示，设置了这个之后， 当有数据接受的时候，会自动触发回调函数
	 * */
	private void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBtAdapter == null || mBtGatt == null) {
			LogUtil.e(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBtGatt.setCharacteristicNotification(characteristic, enabled);

		// 假如定义了以下的函数，就可以随时获得从底层读取上来的数据，假如没有下面的代码，需要先写，才可以读！！
		// This is specific to Heart Rate Measurement.
		if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
			BluetoothGattDescriptor descriptor = characteristic
					.getDescriptor(UUID
							.fromString(BtGattAttr.CLIENT_CHARACTERISTIC_CONFIG));
			descriptor
					.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE); // 使能
																					// NOTIFICATION_VALUE值
			mBtGatt.writeDescriptor(descriptor);
		}

		if (UUID_ISSC_RX.equals(characteristic.getUuid())) {
			BluetoothGattDescriptor descriptor = characteristic
					.getDescriptor(UUID
							.fromString(BtGattAttr.CLIENT_CHARACTERISTIC_CONFIG));
			if (descriptor != null) {
				descriptor
						.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
				mBtGatt.writeDescriptor(descriptor);
			}
		}
	}

	/**
	 * 广播的核心处理,用以处理广播搜索中，对人体秤,脂肪秤的区别
	 * 
	 */
	private void handleBroadcastInfo(final BluetoothDevice device,
			final int rssi, final byte[] scanRecord) {
		//先暂停搜索
		stopSearching2();
		CsDevice bluetoothDevice = new CsDevice();
		
		//判断当前是否有实例
		if (device != null && (device.getName() != null)
				&& (device.getAddress() != null)) {
			bluetoothDevice.setBtMac(device.getAddress().toString());
			bluetoothDevice.setBtName(device.getName().toString());
			LogUtil.i(TAG, "current device MAC:" + device.getAddress() + "");
			LogUtil.i(TAG, "current device Name:" + device.getName() + "");
		}else{
			return;
		}
		if(bluetoothListener != null){
			bluetoothListener.bluetoothStateChange(STATE_BROADCAST);
		}
		//判断当前名字是否与我司的芯片相吻合
		if (bluetoothDevice.getBtName().startsWith(CHIPSEA.BT_NAME)) {
			// ==========================解包广播包数据===============================
			byte[] receiveData = scanRecord; // 得到接收到的广播字节
			int startNum = 5;
			byte receiveLen = receiveData[startNum + 2];

			byte[] receiveFrame = BytesUtil.subBytes(receiveData, startNum,
					receiveLen + 4); // 截取数据帧
			
			if (bluetoothListener != null) {
				bluetoothListener.broadcastData(bluetoothDevice.getBtMac(),receiveFrame);
			}
			// LogUtil.i(TAG,"==> "+BytesUtil.bytesToHexString(receiveFrame));
			if (receiveFrame != null) {
				try {
					Frame frame = new Frame(receiveFrame);
					FrameData frameData = frame.getDatasArea();
					byte[] id = new byte[]{};
					if(receiveFrame[1] == 0x10){
						id = BytesUtil.subBytes(receiveFrame,
								7, 4);
					}
					/**
					 * ===========================蓝牙人体秤========
					 * =====================
					 **/
					if (frameData.getDeviceType() == DataType.WEIGHER) {
						weigher = new CsWeigher();
						// byte[] firmId =
						// BytesUtil.subBytes(frameData.getFull(),
						// 2, 3);
						byte[] weight = BytesUtil.subBytes(frameData.getFull(),
								5, 2);
						byte insCmd = frameData.getFull()[1];
						if ((weight != null) && (bluetoothListener != null)) {
							byte lockFlag = (byte) (insCmd & (byte) 0x01);
							// byte radixPoint = (byte)(
							// (insCmd>>1) & 0x03);
							// byte unit = (byte)( (insCmd >> 3)
							// & 0x0f);
							// Log.v(TAG,"===insCmd=== "+insCmd);
							// Log.v(TAG,"===lockFlag=== "+lockFlag);
							// Log.v(TAG,"===radixPoint=== "+radixPoint);
							// Log.v(TAG,"===unit=== "+unit);
							weigher.setProduct_id(BytesUtil.bytesToInt(id));
							weigher.setWeight(BytesUtil.bytesToInt(weight));
							weigher.setBtMac(bluetoothDevice.getBtMac());
							weigher.setBtName(bluetoothDevice.getBtName());
							if (lockFlag == 0) {
								bluetoothListener.broadcastWeigherInfo(weigher,
										false);
							} else if (lockFlag == 1) {
								bluetoothListener.broadcastWeigherInfo(weigher,
										true);
							}
						}
						/**
						 * ===========================蓝牙脂肪秤====
						 * =========================
						 * */
					} else if (frameData.getDeviceType() == DataType.FAT_SCALE) {
						fatScale = new CsFatScale();
						byte[] weight = BytesUtil.subBytes(frameData.getFull(),
								2, 2);
						byte[] axunge = BytesUtil.subBytes(frameData.getFull(),
								4, 2);
						byte[] water = BytesUtil.subBytes(frameData.getFull(),
								6, 2);
						byte[] muscle = BytesUtil.subBytes(frameData.getFull(),
								8, 2);
						byte[] bmr = BytesUtil.subBytes(frameData.getFull(),
								10, 2);
						byte[] visceral_fat = BytesUtil.subBytes(
								frameData.getFull(), 12, 2);
						byte[] bone = BytesUtil.subBytes(frameData.getFull(),
								14, 1);

						byte lockflag = frameData.getFull()[1];

						if (fatScale != null) {
							fatScale.setProduct_id(BytesUtil.bytesToInt(id));
							fatScale.setBtMac(bluetoothDevice.getBtMac());
							fatScale.setBtName(bluetoothDevice.getBtName());
							fatScale.setWeight(BytesUtil.bytesToInt(weight));
							fatScale.setAxunge(BytesUtil.bytesToInt(axunge));
							fatScale.setWater(BytesUtil.bytesToInt(water));
							fatScale.setMuscle(BytesUtil.bytesToInt(muscle));
							fatScale.setBmr(BytesUtil.bytesToInt(bmr));
							fatScale.setVisceral_fat(BytesUtil
									.bytesToInt(visceral_fat));
							fatScale.setBone(BytesUtil.bytesToInt(bone));
							if (bluetoothListener != null) {
								if (lockflag == 0) {
									bluetoothListener.broadcastFatScaleInfo(
											fatScale, false);
								} else if (lockflag == 1) {
									bluetoothListener.broadcastFatScaleInfo(
											fatScale, true);
								}
							}
						}
					}
				} catch (FrameFormatIllegalException e) {
					// e.printStackTrace();
					// 告诉ui界面接收到数据有问题
				}
			}
		}
		startSearching2();
	}

	
	/**
	 * 连接之后处理的核心,用以处理连接之后的数据
	 */
	private void handleConnectedInfo(BluetoothGattCharacteristic characteristic){

		final byte[] data = characteristic.getValue();
		// 判断是否有数据=============================================
		if (data != null && data.length > 0) {
			// 通知UI，有收到数据
			try {
				Frame frame = new Frame(data);
				FrameData frameData = frame.getDatasArea();
				/**
				 * 1.================================脂肪秤====================
				 * =================
				 */
				if (frameData.getDeviceType() == DataType.FAT_SCALE) {

					// Log.e(TAG,""+BytesUtil.bytesToHexString(frame.getFrameFull()));
					byte[] weight = BytesUtil.subBytes(frameData.getFull(),
							2, 2);
					byte[] axunge = BytesUtil.subBytes(frameData.getFull(),
							4, 2);
					byte[] water = BytesUtil.subBytes(frameData.getFull(),
							6, 2);
					byte[] muscle = BytesUtil.subBytes(frameData.getFull(),
							8, 2);
					byte[] bmr = BytesUtil.subBytes(frameData.getFull(),
							10, 2);
					byte[] visceral_fat = BytesUtil.subBytes(
							frameData.getFull(), 12, 2);
					byte[] bone = BytesUtil.subBytes(frameData.getFull(),
							14, 1);

					byte lockflag = frameData.getFull()[1];

					if (fatScale != null) {
						fatScale.setWeight(BytesUtil.bytesToInt(weight));
						if (lockflag > 0) {
							fatScale.setAxunge(BytesUtil.bytesToInt(axunge));
							fatScale.setWater(BytesUtil.bytesToInt(water));
							fatScale.setMuscle(BytesUtil.bytesToInt(muscle));
							fatScale.setBmr(BytesUtil.bytesToInt(bmr));
							fatScale.setVisceral_fat(BytesUtil
									.bytesToInt(visceral_fat));
							fatScale.setBone(BytesUtil.bytesToInt(bone));
						}
					}

					if (bluetoothListener != null) {
						if (lockflag == 0) {
							bluetoothListener.specialFatScaleInfo(fatScale,
									false);
						} else {
							bluetoothListener.specialFatScaleInfo(fatScale,
									true);
						}
						bluetoothListener.bluetoothStateChange(STATE_READING);
					}
				} else {

				}

			} catch (FrameFormatIllegalException e) {
				// e.printStackTrace();
			}
		}
	}
	
	// ========================================自定义类接口======================================
	// ========================================自定义类接口======================================
	// ========================================自定义类接口======================================
	public interface OnBluetoothListener {

		/**
		 * 广播获得的数据
		 * 
		 * @param advData
		 */
		void broadcastData(String mac, byte[] advData);

		/**
		 * 广播获得的人体秤数据
		 * 
		 * @param mac
		 * @param weight
		 * @param isLock
		 */
		void broadcastWeigherInfo(CsWeigher weigher, boolean isLock);

		/**
		 * 广播获得的脂肪秤数据
		 * 
		 * @param mac
		 * @param weight
		 * @param axunge
		 * @param bone
		 * @param muscle
		 * @param water
		 * @param metabolism
		 * @param bodyAge
		 * @param viscera
		 * @param isLock
		 */
		void broadcastFatScaleInfo(CsFatScale fatScale, boolean isLock);

		/**
		 * 透传获得的脂肪秤数据
		 *
		 * @param mac
		 * @param weight
		 * @param axunge
		 * @param bone
		 * @param muscle
		 * @param water
		 * @param metabolism
		 * @param bodyAge
		 * @param viscera
		 * @param isLock
		 */
		void specialFatScaleInfo(CsFatScale fatScale, boolean isLock);

		/**
		 * 透传的时候捕获读写句柄--透传连接成功之后回调
		 * 
		 * @param notify
		 * @param write
		 */
		void didConnectedGetWriteNotifyCharacteristic(
				BluetoothGattCharacteristic notify,
				BluetoothGattCharacteristic write);
		void bluetoothStateChange(int state);
		void disconnectBluetooth();
		
	}
}
