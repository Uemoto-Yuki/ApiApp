package jp.techacademy.yuki.uemoto.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_api.*
import kotlinx.android.synthetic.main.recycler_favorite.*
import io.realm.Realm
import io.realm.RealmObject

class WebViewActivity : AppCompatActivity() {


    companion object {
        const val KEY_SHOP = "key_shop"
        fun start(activity: Activity, shop: Shop) {
            activity.startActivity(
                Intent(activity, WebViewActivity::class.java).putExtra(KEY_SHOP, shop)
            )
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val shop = intent.getSerializableExtra(KEY_SHOP) as Shop //asでShop型として変換して代入
        webView.loadUrl(if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc)

        var isFavorite =
            FavoriteShop.findBy(shop.id) != null//お気に入りされているShopをidで検索して返す。お気に入りに登録されていなければnullで返す

        //ここでお気に入りに入ってればFloatingbuttonに表示させる
        favoriteImageView.apply {
            favorite.setImageResource(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border)
        }


        favorite.setOnClickListener {

            if (isFavorite == true) {

                FavoriteShop.delete(shop.id)

                favorite.setImageResource(R.drawable.ic_star_border)
                isFavorite = false
                Toast.makeText(applicationContext, "お気に入りから削除しました",Toast.LENGTH_SHORT).show()



            } else {
                FavoriteShop.insert(FavoriteShop().apply {
                    id = shop.id
                    name = shop.name
                    address = shop.address
                    imageUrl = shop.logoImage
                    url =
                        if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
                })
                favorite.setImageResource(R.drawable.ic_star)
                Toast.makeText(applicationContext, "お気に入りに追加しました",Toast.LENGTH_SHORT).show()

                isFavorite = true
            }
            // MainActivityに戻った時に、更新かけなくても星imageが変わっているように

        }

    }


}