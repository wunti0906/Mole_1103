package tw.edu.pu.csim.tcyang.mole

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MoleViewModel : ViewModel(){
    var counter by mutableLongStateOf(0)
        private set  //表示只有 ViewModel 內部可以修改它

    fun incrementCounter() {
        counter++
    }

    var stay by mutableLongStateOf(0)
        private set // 設定為 private

    var maxX by mutableStateOf(0)
        private set

    var maxY by mutableStateOf(0)
        private set

    var offsetX by mutableStateOf(0)
        private set

    var offsetY by mutableStateOf(0)
        private set


    init {
        // 在 ViewModel 初始化時啟動一個協程來自動增加計數器
        startCounting()
    }

    private fun startCounting() {
        viewModelScope.launch {
            while (true) { // 無限循環，每秒增加一次
                delay(1000L)
                stay++ // 計數器加 1，這會自動觸發 UI 更新
                moveMole()
            }
        }
    }
    fun getArea(gameSize: IntSize, moleSize:Int) {
        maxX = gameSize.width - moleSize
        maxY = gameSize.height - moleSize
    }

    fun moveMole() {
        offsetX = (0..maxX).random()
        offsetY = (0..maxY).random()
    }


}
