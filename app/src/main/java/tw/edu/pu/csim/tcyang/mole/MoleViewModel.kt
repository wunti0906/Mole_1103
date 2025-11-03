package tw.edu.pu.csim.tcyang.mole

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MoleViewModel : ViewModel() {
    // 遊戲常數
    private val GAME_DURATION_SECONDS = 60
    private val MOLE_MOVE_DELAY_MS = 1000L // 地鼠每 1 秒移動一次

    // ========== 遊戲狀態 (State) ==========
    var counter = mutableIntStateOf(0) // 分數
    var stay = mutableIntStateOf(GAME_DURATION_SECONDS) // 時間 (倒數計時)
    var isGameOver = mutableStateOf(false) // 遊戲是否結束

    // ========== 地鼠位置狀態 (State) ==========
    var offsetX = mutableIntStateOf(0)
    var offsetY = mutableIntStateOf(0)

    // ========== 螢幕尺寸參數 ==========
    private var maxX = 0
    private var maxY = 0
    private var moleSizePx = 0

    init {
        startGame()
    }

    /**
     * 接收螢幕尺寸和地鼠尺寸，用來計算地鼠可移動的範圍。
     * (在 MoleScreen 的 onSizeChanged 中呼叫)
     */
    fun getArea(screenSize: IntSize, moleSize: Int) {
        moleSizePx = moleSize
        // 可移動的範圍 = 螢幕尺寸 - 地鼠尺寸
        maxX = screenSize.width - moleSizePx
        maxY = screenSize.height - moleSizePx

        // 首次設定位置
        if (offsetX.intValue == 0 && offsetY.intValue == 0) {
            moveMoleRandomly()
        }
    }

    /**
     * 點擊地鼠時呼叫，檢查遊戲是否結束並加分。
     */
    fun incrementCounter() {
        // 只有在遊戲未結束時才允許加分
        if (!isGameOver.value) {
            counter.intValue++
            // 點擊後立即移動地鼠
            moveMoleRandomly()
        }
    }

    /**
     * 隨機移動地鼠位置。
     */
    private fun moveMoleRandomly() {
        // 確保範圍有效
        if (maxX > 0 && maxY > 0) {
            // 在 0 到 最大範圍 之間取隨機數
            offsetX.intValue = Random.nextInt(maxX.coerceAtLeast(1))
            offsetY.intValue = Random.nextInt(maxY.coerceAtLeast(1))
        }
    }

    /**
     * 啟動遊戲計時器和地鼠移動循環。
     */
    private fun startGame() {
        // 1. 啟動計時器 (倒數 60 秒)
        viewModelScope.launch {
            while (stay.intValue > 0) {
                delay(1000L) // 等待 1 秒
                stay.intValue--

                if (stay.intValue <= 0) {
                    isGameOver.value = true // 時間到，設定遊戲結束
                }
            }
        }

        // 2. 啟動地鼠移動循環 (遊戲結束時停止)
        viewModelScope.launch {
            // 只有在遊戲未結束時才繼續移動
            while (!isGameOver.value) {
                moveMoleRandomly()
                delay(MOLE_MOVE_DELAY_MS) // 等待 1 秒後再次移動
            }
        }
    }
}