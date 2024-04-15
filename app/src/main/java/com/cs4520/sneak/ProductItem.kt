package com.cs4520.sneak

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.model.ProductViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun GlideImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Drawable? = null
) {
    var image: ImageBitmap? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    DisposableEffect(imageUrl) {
        val target = object : CustomTarget<android.graphics.Bitmap>() {
            override fun onResourceReady(resource: android.graphics.Bitmap, transition: Transition<in android.graphics.Bitmap>?) {
                image = resource.asImageBitmap()
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                image = null
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                image = null
            }
        }

        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .placeholder(placeholder)
            .into(target)

        onDispose {
            Glide.with(context).clear(target)
        }
    }

    image?.let {
        Image(
            bitmap = it,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}


fun getImageUrlForManufacturer(manufacturer: String): String {
    return when (manufacturer) {
        "Nike" -> "https://res.cloudinary.com/dmubfrefi/image/private/s--X0LLoOBX--/c_crop,h_2728,w_4090,x_334,y_0/f_auto/q_auto/v1/dee-about-cms-prod-medias/cf68f541-fc92-4373-91cb-086ae0fe2f88/002-nike-logos-swoosh-white.jpg?_a=BAAAROBs"
        "Adidas" -> "https://www.shutterstock.com/image-vector/valencia-spain-april-25-2023-600nw-2293213071.jpg"
        "Vans" -> "https://static.vecteezy.com/system/resources/previews/024/455/429/non_2x/vans-brand-logo-white-symbol-design-icon-abstract-illustration-with-black-background-free-vector.jpg"
        "Reebok" -> "https://static.vecteezy.com/system/resources/previews/023/869/483/original/reebok-logo-brand-clothes-with-name-red-and-blue-symbol-design-icon-abstract-illustration-free-vector.jpg"
        "Converse" -> "https://cdn.shopify.com/s/files/1/0558/6413/1764/files/Rewrite_Converse_Logo_Design_History_Evolution_0_1024x1024.jpg?v=1694703129"
        "ASICS" -> "https://cdn.freebiesupply.com/logos/thumbs/2x/asics-6-logo.png"
        else -> "https://example.com/default-shoe-image.jpg" // Default image if none match
    }
}



@Composable
fun ProductItem(shoe: Shoe, viewModel: ProductViewModel) {
    val cartItems = viewModel.cartItems.observeAsState(listOf()).value

    Log.d("ProductItemTag", "Shoe Type is " + shoe.manufacturer)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,

            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = when (shoe.manufacturer) {
                        "Adidas" -> colorResource(id = R.color.adidas_color)
                        "Converse" -> colorResource(id = R.color.converse_color)
                        "Nike" -> colorResource(id = R.color.nike_color)
                        "ASICS" -> colorResource(id = R.color.asics_color)
                        "Vans" -> colorResource(id = R.color.vans_color)
                        "Reebok" -> colorResource(id = R.color.reebok_color)
                        else -> colorResource(id = R.color.default_shoe_color) // Default color if none match
                    }
                )
                .padding(16.dp)
        ) {
            GlideImage(
                imageUrl = getImageUrlForManufacturer(shoe.manufacturer),
                contentDescription = "Shoe Image",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = shoe.name, style = MaterialTheme.typography.h6)
                Text(
                    text = "Manufacturer: ${shoe.manufacturer}",
                    style = MaterialTheme.typography.body1
                )
                Text(text = "Type: ${shoe.type}", style = MaterialTheme.typography.body2)
                Text(text = "Price: $${shoe.price}", style = MaterialTheme.typography.body2)
                // Add button to add item to cart

            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                // create custom modifier Alignment
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        viewModel.toggleCartItem(shoe)
                    },
                    enabled = true,
                    modifier = Modifier.align(Alignment.Bottom)
                ) {
                    Text(if (cartItems.any { it.name == shoe.name }) "Added" else "Add")
                }
            }
        }
    }
}