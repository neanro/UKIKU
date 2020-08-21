package knf.kuma.recommended

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import knf.kuma.R
import knf.kuma.ads.showRandomInterstitial
import knf.kuma.backup.firestore.syncData
import knf.kuma.commons.EAHelper
import knf.kuma.commons.PrefsUtil
import knf.kuma.commons.safeShow
import knf.kuma.commons.setSurfaceBars
import knf.kuma.custom.GenericActivity
import knf.kuma.database.CacheDB
import kotlinx.android.synthetic.main.recycler_ranking.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.doAsync

class RankingActivityMaterial : GenericActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(EAHelper.getTheme())
        super.onCreate(savedInstanceState)
        setSurfaceBars()
        setContentView(R.layout.recycler_ranking_material)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(false)
            title = "Ranking"
        }
        with(toolbar) {
            setNavigationIcon(R.drawable.ic_close)
            setNavigationOnClickListener { finish() }
        }
        with(recycler) {
            layoutManager = LinearLayoutManager(this@RankingActivityMaterial)
            lifecycleScope.launch(Dispatchers.Main){
                adapter = RankingAdapterMaterial(withContext(Dispatchers.IO) { CacheDB.INSTANCE.genresDAO().ranking })
            }
        }
        setResult(1234)
        showRandomInterstitial(this, PrefsUtil.fullAdsExtraProbability)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_rating, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear ->
                MaterialDialog(this@RankingActivityMaterial).safeShow {
                    message(text = "¿Deseas reiniciar la puntuación de todos los géneros?")
                    positiveButton(text = "continuar") {
                        setResult(4321)
                        doAsync {
                            CacheDB.INSTANCE.genresDAO().reset()
                            syncData { genres() }
                        }
                        finish()
                    }
                    negativeButton(text = "cancelar")
                }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        fun open(activity: Activity) {
            activity.startActivityForResult(Intent(activity, RankingActivityMaterial::class.java), 46897)
        }
    }
}
