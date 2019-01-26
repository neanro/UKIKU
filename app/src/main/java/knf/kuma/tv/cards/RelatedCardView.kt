package knf.kuma.tv.cards

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import knf.kuma.R
import knf.kuma.commons.PatternUtil
import knf.kuma.commons.load
import knf.kuma.database.CacheDB
import knf.kuma.pojos.AnimeObject
import knf.kuma.tv.BindableCardView
import kotlinx.android.synthetic.main.item_tv_card_chapter.view.*

class RelatedCardView(context: Context) : BindableCardView<AnimeObject.WebInfo.AnimeRelated>(context) {

    override val imageView: ImageView
        get() = img
    override val layoutResource: Int
        get() = R.layout.item_tv_card_chapter

    override fun bind(data: AnimeObject.WebInfo.AnimeRelated) {
        val animeObject = CacheDB.INSTANCE.animeDAO().getByLink("%" + data.link)
        if (animeObject != null)
            img.load(PatternUtil.getCover(data.aid))
        else
            img.load(Uri.EMPTY)
        title?.text = data.name
        chapter?.text = data.relation
    }
}
