package com.nova.assistant.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Command(
    val id: String,
    val name: String,
    val description: String,
    val category: CommandCategory,
    val voiceTriggers: List<String>,
    val iconRes: Int,
    val action: CommandAction,
    val parameters: Map<String, String> = emptyMap(),
    val requiresPermission: String? = null,
    val isEnabled: Boolean = true
) : Parcelable

enum class CommandCategory(val displayName: String, val iconRes: Int) {
    ALL("All Commands", android.R.drawable.ic_menu_view),
    DEVICE_CONTROL("Device Control", android.R.drawable.ic_menu_preferences),
    COMMUNICATION("Communication", android.R.drawable.ic_menu_call),
    ALARMS_REMINDERS("Alarms & Reminders", android.R.drawable.ic_menu_recent_history),
    UTILITIES("Utilities", android.R.drawable.ic_menu_manage),
    MEDIA("Media", android.R.drawable.ic_media_play),
    PRODUCTIVITY("Productivity", android.R.drawable.ic_menu_edit),
    WEB_TOOLS("Web & Tools", android.R.drawable.ic_menu_search),
    FUN_EXTRAS("Fun & Extras", android.R.drawable.ic_menu_slideshow),
    SYSTEM_SECURITY("System & Security", android.R.drawable.ic_menu_info_details),
    FILE_MANAGER("File Manager", android.R.drawable.ic_menu_sort_by_size)
}

enum class CommandAction {
    // Device Control
    TOGGLE_WIFI,
    TOGGLE_MOBILE_DATA,
    TOGGLE_HOTSPOT,
    TOGGLE_BLUETOOTH,
    TOGGLE_FLASHLIGHT,
    TOGGLE_AIRPLANE_MODE,
    OPEN_APP,
    CLOSE_APP,
    INCREASE_BRIGHTNESS,
    DECREASE_BRIGHTNESS,
    TOGGLE_DND,
    GET_BATTERY_STATUS,
    
    // Communication
    MAKE_CALL,
    SEND_SMS,
    SEND_WHATSAPP,
    READ_MESSAGES,
    SHOW_CALL_LOG,
    BLOCK_NUMBER,
    
    // Alarms & Reminders
    SET_ALARM,
    SET_TIMER,
    CREATE_REMINDER,
    VIEW_ALARMS,
    CANCEL_ALARM,
    
    // Utilities
    OPEN_CALCULATOR,
    OPEN_CAMERA,
    OPEN_CALENDAR,
    START_SCREEN_RECORDING,
    TAKE_SCREENSHOT,
    SHOW_STORAGE_INFO,
    SCAN_QR_CODE,
    GENERATE_QR_CODE,
    SHARE_APP,
    
    // Media
    PLAY_MUSIC,
    PAUSE_MUSIC,
    NEXT_TRACK,
    PREVIOUS_TRACK,
    PLAY_VIDEO,
    OPEN_YOUTUBE,
    ADJUST_VOLUME,
    LAUNCH_SPOTIFY,
    RECORD_AUDIO,
    
    // Productivity
    ADD_CALENDAR_EVENT,
    CREATE_NOTE,
    ADD_TASK,
    VIEW_SCHEDULE,
    VOICE_NOTE,
    
    // Web & Tools
    OPEN_WEBSITE,
    GOOGLE_SEARCH,
    TRANSLATE_TEXT,
    CONVERT_CURRENCY,
    GET_WEATHER,
    GET_NEWS,
    
    // Fun & Extras
    TELL_JOKE,
    FLIP_COIN,
    ROLL_DICE,
    ROCK_PAPER_SCISSORS,
    SHOW_MEME,
    
    // System & Security
    LOCK_SCREEN,
    REBOOT_DEVICE,
    CLEAR_CACHE,
    ENABLE_VPN,
    CHECK_INTERNET_SPEED,
    
    // File Manager
    BROWSE_FILES,
    OPEN_FILE,
    DELETE_FILE,
    SHARE_FILE,
    RENAME_FILE
}

@Parcelize
data class CommandResult(
    val success: Boolean,
    val message: String,
    val data: String? = null
) : Parcelable