package tw.edu.pu.csim.tcyang.mole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import tw.edu.pu.csim.tcyang.mole.ui.theme.MoleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoleTheme {
                // R.drawable.mole 圖片需存在於 res/drawable/ 資料夾中
                MoleScreen()
            }
        }
    }
}

@Composable
fun MoleScreen(moleViewModel: MoleViewModel = viewModel()) {
    // 獲取 ViewModel 的狀態值
    val counter = moleViewModel.counter.intValue
    val stay = moleViewModel.stay.intValue // 時間
    val offsetX = moleViewModel.offsetX.intValue
    val offsetY = moleViewModel.offsetY.intValue
    val isGameOver = moleViewModel.isGameOver.value

    // 獲取密度並計算地鼠的像素尺寸
    val density = LocalDensity.current
    val moleSizeDp = 150.dp
    val moleSizePx = with(density) { moleSizeDp.roundToPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            // 螢幕尺寸變更時，將新的尺寸傳遞給 ViewModel
            .onSizeChanged { intSize -> moleViewModel.getArea(intSize, moleSizePx) },
        contentAlignment = Alignment.Center // 將所有文字內容置中
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "打地鼠遊戲 (林彣媞)", // 👈 遊戲標題和作者
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            // 根據遊戲狀態顯示分數/時間或遊戲結束訊息
            Text(
                text = if (isGameOver) {
                    "遊戲結束！最終分數：$counter"
                } else {
                    "分數: $counter \n時間: $stay"
                },
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }

    // 只有在遊戲未結束時才顯示地鼠圖片
    if (!isGameOver) {
        Image(
            painter = painterResource(id = R.drawable.mole), // 確保你有名為 mole 的圖片
            contentDescription = "地鼠",
            modifier = Modifier
                // 使用 ViewModel 提供的動態座標
                .offset { IntOffset(offsetX, offsetY) }
                .size(moleSizeDp)
                // 點擊後呼叫 ViewModel 的加分/移動方法
                .clickable { moleViewModel.incrementCounter() }
        )
    }
}

