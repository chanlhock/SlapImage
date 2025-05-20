import com.example.slapimage.xededitor.controlpanel.showControlPanel
import com.example.slapimage.xededitor.libcommons.toast
import com.example.slapimage.xededitor.resources.strings
import com.example.slapimage.xededitor.xededitor.MainActivity.XEDMainActivity

//just a class to prevent use of XEDMainActivity.withContext where its not necessary
object CheapEventBus {
    fun showControlPanel(){
        XEDMainActivity.withContext { this.showControlPanel() }
    }
}