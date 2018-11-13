package ir.tapsell.sdk.b4a;

import ir.tapsell.sdk.TapsellB4A;
import ir.tapsell.sdk.TapsellB4AListener;
import ir.tapsell.sdk.bannerads.TapsellBannerType;
import ir.tapsell.sdk.bannerads.TapsellBannerView;
import ir.tapsell.sdk.bannerads.TapsellBannerViewEventListener;
import ir.tapsell.sdk.nativeads.TapsellNativeBannerAdLoader;
import ir.tapsell.sdk.nativeads.TapsellNativeVideoAdLoadListener;
import ir.tapsell.sdk.nativeads.TapsellNativeVideoAdLoader;
import ir.tapsell.sdk.nativeads.TapsellNativeBannerAdLoadListener;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import android.Manifest;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import anywheresoftware.b4a.BA;

@BA.ActivityObject
@BA.ShortName("Tapsell")
@BA.Permissions(values = { Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE })
@BA.Version(1f)
@BA.Events(values = { "onAdShowFinished (zoneId As String, adId As String, completed As Boolean, rewarded As Boolean)",
		"onAdAvailable (zoneId As String, adId As String)", "onNoAdAvailable (zoneId As String)",
		"onNoNetwork (zoneId As String)", "onExpiring (zoneId As String, adId As String)",
		"onOpened (zoneId As String, adId As String)", "onClosed (zoneId As String, adId As String)",
		"onError(zoneId As String, error As String)", "onNativeBannerAdAvailable (zoneId As String, adId As String)",
		"onNoNativeBannerAdAvailable (zoneId As String)", "onNativeVideoAdAvailable (zoneId As String, adId As String)",
		"onNoNativeVideoAdAvailable (zoneId As String)", "onNoBannerAdAvailable (zoneId As String)",
		"onBannerNoNetwork (zoneId As String)", "onBannerAdRequestFilled (zoneId As String)",
		"onBannerAdMadeHidden (zoneId As String)", "onBannerAdError (error As String , zoneId As String)" })
public class Tapsell implements TapsellB4AListener {

	@BA.Hide
	private static BA mBA;

	public static final int ROTATION_LOCKED_PORTRAIT = 1;

	public static final int ROTATION_LOCKED_LANDSCAPE = 2;

	public static final int ROTATION_UNLOCKED = 3;

	public static final int ROTATION_LOCKED_REVERSED_PORTRAIT = 4;

	public static final int ROTATION_LOCKED_REVERSED_LANDSCAPE = 5;

	public static final int BANNER_320x50 = 1;

	public static final int BANNER_320x100 = 2;

	public static final int BANNER_250x250 = 3;

	public static final int BANNER_300x250 = 4;

	private static final Map<String, TapsellBannerView> bannerList = Collections
			.synchronizedMap(new WeakHashMap<String, TapsellBannerView>());

	private final Handler mHandler = new Handler(Looper.getMainLooper());

	public boolean initialize(BA ba, String appKey) {
		mBA = ba;
		TapsellB4A.setB4AListener(this);
		return TapsellB4A.initialize(ba.activity, appKey, "4.0.0");
	}

	public boolean requestAd(String zoneId, boolean isCached) {
		if (zoneId != null && (zoneId.equalsIgnoreCase("null") || zoneId.equalsIgnoreCase(""))) {
			zoneId = null;
		}

		if (mBA == null) {
			return false;
		}

		boolean ret = TapsellB4A.requestAd(mBA.activity, zoneId, isCached);
		Log.e("B4A", "requestAd: " + ret);
		return ret;
	}

	public final String getVersion() {
		return TapsellB4A.getVersion();
	}

	public void showAd(String id, boolean back_disabled, boolean immersive_mode, int rotation_mode,
			boolean show_dialog) {
		if (mBA == null) {
			return;
		}

		TapsellB4A.showAd(mBA.activity, id, back_disabled, immersive_mode, rotation_mode, show_dialog);
	}

	public void setDebugMode(boolean debug) {
		if (mBA == null) {
			return;
		}

		TapsellB4A.setDebugMode(mBA.activity, debug);
	}

	public boolean isDebugMode() {
		if (mBA == null) {
			return false;
		}

		return TapsellB4A.isDebugMode(mBA.activity);
	}

	public static void setMaxAllowedBandwidthUsage(int maxBpsSpeed) {
		TapsellB4A.setMaxAllowedBandwidthUsage(mBA.activity, maxBpsSpeed);
	}

	public static void setMaxAllowedBandwidthUsagePercentage(int maxPercentage) {
		TapsellB4A.setMaxAllowedBandwidthUsagePercentage(mBA.activity, maxPercentage);
	}

	public static void clearBandwidthUsageConstrains() {
		TapsellB4A.clearBandwidthUsageConstrains(mBA.activity);
	}

	public static void setPer(int config) {
		TapsellB4A.setPermissionHandlerConfig(mBA.activity, config);
	}

	public void setAppUserId(String appUserId) {
		if (mBA == null) {
			return;
		}

		TapsellB4A.setAppUserId(mBA.activity, appUserId);
	}

	public String getAppUserId() {
		if (mBA == null) {
			return null;
		}

		return TapsellB4A.getAppUserId(mBA.activity);
	}

	@Override
	public void onAdShowFinished(final String zoneId, final String adId, final boolean completed,
			final boolean rewarded) {

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mBA != null) {
					if (mBA.subExists("tapsell_onadshowfinished")) {
						String mZoneId = zoneId;
						if (mZoneId == null) {
							mZoneId = "Null";
						}

						Log.e("B4A", "onAdShowFinished, zoneId: " + mZoneId + ", add: " + adId + ", completed? "
								+ completed + ", rewarded? " + rewarded);

						mBA.raiseEvent2(mBA, true, "tapsell_onadshowfinished", false, mZoneId, adId, completed,
								rewarded);
					}
				}
			}
		});
	}

	@Override
	public void onAdAvailable(final String zoneId, final String adId) {
		Log.e("B4A", "onAdAvailable");

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mBA != null) {
					if (mBA.subExists("tapsell_onadavailable")) {
						mBA.raiseEvent(mBA, "tapsell_onadavailable", zoneId, adId);
					}
				}
			}
		});
	}

	@Override
	public void onNoAdAvailable(final String zoneId) {
		Log.e("B4A", "onNoAdAvailable");

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mBA != null) {
					if (mBA.subExists("tapsell_onnoadavailable")) {
						mBA.raiseEvent(mBA, "tapsell_onnoadavailable", zoneId);
					}
				}
			}
		});
	}

	@Override
	public void onNoNetwork(final String zoneId) {
		Log.e("B4A", "onNoNetwork");

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mBA != null) {
					if (mBA.subExists("tapsell_onnonetwork")) {
						mBA.raiseEvent(mBA, "tapsell_onnonetwork", zoneId);
					}
				}
			}
		});
	}

	@Override
	public void onError(final String zoneId, final String error) {
		Log.e("B4A", "onError: " + error);

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mBA != null) {
					if (mBA.subExists("tapsell_onerror")) {
						mBA.raiseEvent(mBA, "tapsell_onerror", zoneId, error);
					}
				}
			}
		});
	}

	@Override
	public void onExpiring(final String zoneId, final String adId) {
		Log.e("Tapsell", "onExpiring");

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mBA != null) {
					if (mBA.subExists("tapsell_onexpiring")) {
						mBA.raiseEvent2(mBA, true, "tapsell_onexpiring", false, zoneId, adId);
					}
				}
			}
		});
	}

	@Override
	public void onOpened(final String zoneId, final String adId) {
		Log.e("Tapsell", "onOpened");

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mBA != null) {
					if (mBA.subExists("tapsell_onopened")) {
						mBA.raiseEvent2(mBA, true, "tapsell_onopened", false, zoneId, adId);
					}
				}
			}
		});

	}

	@Override
	public void onClosed(final String zoneId, final String adId) {
		Log.e("Tapsell", "onClosed");

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mBA != null) {
					if (mBA.subExists("tapsell_onclosed")) {
						mBA.raiseEvent2(mBA, true, "tapsell_onclosed", false, zoneId, adId);
					}
				}
			}
		});

	}

	public void fillNativeBannerAd(final String zoneId, TextView tvTitle, TextView tvDescription, ImageView ivBanner,
			ImageView ivLogo, TextView tvCTAButton, TextView tvSponsored, ViewGroup adContainer) {
		if (mBA == null) {
			return;
		}

		TapsellNativeBannerAdLoader.fillNativeBannerAd(mBA.activity, zoneId, tvTitle, tvDescription, ivBanner, ivLogo,
				tvCTAButton, tvSponsored, adContainer, new TapsellNativeBannerAdLoadListener() {

					@Override
					public void onNoNetwork() {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								if (mBA != null) {
									if (mBA.subExists("tapsell_onnonetwork")) {
										mBA.raiseEvent(mBA, "tapsell_onnonetwork", zoneId);
									}
								}
							}
						});
					}

					@Override
					public void onNoAdAvailable() {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								if (mBA != null) {
									if (mBA.subExists("tapsell_onnonativebanneradavailable")) {
										mBA.raiseEvent(mBA, "tapsell_onnonativebanneradavailable", zoneId);
									}
								}
							}
						});
					}

					@Override
					public void onError(final String error) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								if (mBA != null) {
									if (mBA.subExists("tapsell_onerror")) {
										mBA.raiseEvent(mBA, "tapsell_onerror", zoneId, error);
									}
								}
							}
						});
					}

					@Override
					public void onRequestFilled(final String adId) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								if (mBA != null) {
									if (mBA.subExists("tapsell_onnativebanneradavailable")) {
										mBA.raiseEvent(mBA, "tapsell_onnativebanneradavailable", zoneId, adId);
									}
								}
							}
						});

					}
				});

	}

	public void fillNativeVideoAd(final String zoneId, boolean autoStartVideo, boolean muteVideo,
			boolean muteVideoBtnEnabled, TextView tvTitle, TextView tvDescription, ViewGroup videoView,
			ImageView ivLogo, TextView tvCTAButton, TextView tvSponsored, ViewGroup parentView) {

		if (mBA == null) {
			return;
		}

		TapsellNativeVideoAdLoader.fillNativeVideoAd(mBA.activity, zoneId, autoStartVideo, false, muteVideo,
				muteVideoBtnEnabled, tvTitle, tvDescription, videoView, ivLogo, tvCTAButton, tvSponsored, parentView,
				new TapsellNativeVideoAdLoadListener() {

					@Override
					public void onRequestFilled(final String adId) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								if (mBA != null) {
									if (mBA.subExists("tapsell_onnativevideoadavailable")) {
										mBA.raiseEvent(mBA, "tapsell_onnativevideoadavailable", zoneId, adId);
									}
								}
							}
						});
					}

					@Override
					public void onNoNetwork() {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								if (mBA != null) {
									if (mBA.subExists("tapsell_onnonetwork")) {
										mBA.raiseEvent(mBA, "tapsell_onnonetwork", zoneId);
									}
								}
							}
						});
					}

					@Override
					public void onNoAdAvailable() {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								if (mBA != null) {
									if (mBA.subExists("tapsell_onnonativevideoadavailable")) {
										mBA.raiseEvent(mBA, "tapsell_onnonativevideoadavailable", zoneId);
									}
								}
							}
						});
					}

					@Override
					public void onError(final String error) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								if (mBA != null) {
									if (mBA.subExists("tapsell_onerror")) {
										mBA.raiseEvent(mBA, "tapsell_onerror", zoneId, error);
									}
								}
							}
						});
					}
				});
	}

	public void setBannerVisibality(String zoneId, boolean visible) {
		TapsellBannerView bnnerView = getBannerView(zoneId);
		if (bnnerView == null) {
			return;
		}

		if (visible) {
			bnnerView.showBannerView();
		} else {
			bnnerView.hideBannerView();
		}
	}

	private void cachBannerView(TapsellBannerView bannerView) {
		if (bannerView != null && bannerView.getZoneId() != null) {
			bannerList.put(bannerView.getZoneId(), bannerView);
		}
	}

	private void removeCachedBannerView(String zoneId) {
		if (zoneId != null) {
			bannerList.remove(zoneId);
		}
	}

	private TapsellBannerView getBannerView(String zoneId) {
		return bannerList.get(zoneId);
	}

	public void fillBannerAd(ViewGroup adContainer, final String zoneId, int bannerType) {
		TapsellBannerView bannerAdView = new TapsellBannerView(mBA.activity, TapsellBannerType.fromValue(bannerType),
				zoneId);

		bannerAdView.setEventListener(new TapsellBannerViewEventListener() {

			@Override
			public void onRequestFilled() {
				// TODO Auto-generated method stub
				onBannerAdRequestFilled(zoneId);
			}

			@Override
			public void onNoNetwork() {
				// TODO Auto-generated method stub
				onBannerNoNetwork(zoneId);
			}

			@Override
			public void onNoAdAvailable() {
				// TODO Auto-generated method stub
				onNoBannerAdAvailable(zoneId);
			}

			@Override
			public void onHideBannerView() {
				// TODO Auto-generated method stub
				onBannerAdMadeHidden(zoneId);
			}

			@Override
			public void onError(String error) {
				// TODO Auto-generated method stub
				onBannerAdError(error, zoneId);
			}
		});

		adContainer.addView(bannerAdView,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		cachBannerView(bannerAdView);
	}

	@Override
	public void onBannerAdError(final String error, final String zoneId) {
		// TODO Auto-generated method stub
		Log.e("Tapsell", "onBannerAdError");

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mBA != null) {
					if (mBA.subExists("tapsell_onbanneraderror")) {
						mBA.raiseEvent2(mBA, true, "tapsell_onbanneraderror", false, error, zoneId);
					}
				}
			}
		});
	}

	@Override
	public void onBannerAdMadeHidden(final String zoneId) {
		// TODO Auto-generated method stub
		Log.e("Tapsell", "onBannerAdMadeHidden");

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mBA != null) {
					if (mBA.subExists("tapsell_onbanneradmadehidden")) {
						mBA.raiseEvent2(mBA, true, "tapsell_onbanneradmadehidden", false, zoneId);
					}
				}
			}
		});
	}

	@Override
	public void onBannerAdRequestFilled(final String zoneId) {
		// TODO Auto-generated method stub
		Log.e("Tapsell", "onBannerAdRequestFilled");

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mBA != null) {
					if (mBA.subExists("tapsell_onbanneradrequestfilled")) {
						mBA.raiseEvent2(mBA, true, "tapsell_onbanneradrequestfilled", false, zoneId);
					}
				}
			}
		});
	}

	@Override
	public void onBannerNoNetwork(final String zoneId) {
		// TODO Auto-generated method stub
		Log.e("Tapsell", "onBannerNoNetwork");

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mBA != null) {
					if (mBA.subExists("tapsell_onbannernonetwork")) {
						mBA.raiseEvent2(mBA, true, "tapsell_onbannernonetwork", false, zoneId);
					}
				}
			}
		});
	}

	@Override
	public void onNoBannerAdAvailable(final String zoneId) {
		// TODO Auto-generated method stub
		Log.e("Tapsell", "onNoBannerAdAvailable");

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mBA != null) {
					if (mBA.subExists("tapsell_onnobanneradavailable")) {
						mBA.raiseEvent2(mBA, true, "tapsell_onnobanneradavailable", false, zoneId);
					}
				}
			}
		});
	}

}
