package com.cs4520.sneak

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.model.ProductViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color


@Composable
fun ProductItem(shoe: Shoe, viewModel: ProductViewModel) {
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
        ) { /*
            // Use Glide for loading images
            val painter = rememberImagePainter(
                request = when (shoe.manufacturer) {
                    "Adidas" -> R.drawable.adidas
                    "Converse" -> R.drawable.converse
                    "Nike" -> R.drawable.nike
                    "ASICS" -> R.drawable.asics
                    "Vans" -> R.drawable.vans
                    "Reebok" -> R.drawable.reebok
                    else -> R.drawable.equipment // Default image if none match
                }
            )
            Image(
                painter = painter,
                contentDescription = "Shoe Image",
                modifier = Modifier
                    .size(100.dp), // Ensures the image is always 60dp x 60dp
                contentScale = ContentScale.Crop
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
            } */
            Image(
                painter = painterResource(
                    id = when (shoe.manufacturer) {
                        "Adidas" -> R.drawable.adidas
                        "Converse" -> R.drawable.converse
                        "Nike" -> R.drawable.nike
                        "ASICS" -> R.drawable.asics
                        "Vans" -> R.drawable.vans
                        "Reebok" -> R.drawable.reebok
                        else -> R.drawable.equipment // Default image if none match
                    }
                ),
                contentDescription = "Shoe Image",
                modifier = Modifier
                    .size(100.dp), // Ensures the image is always 60dp x 60dp
                    //.clip(CircleShape), // Optional: Clips the image to a circle
                contentScale = ContentScale.Crop
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
            }
            // Add button to add item to cart
            Button(
                onClick = {
                    viewModel.toggleCartItem(shoe)
                },
                enabled = true, // Ensure the button is enabled
                modifier = Modifier.align(Alignment.Bottom),
            ) {
                Text(if (viewModel.cartItems.value.toMutableList().contains(shoe.name)) "Added" else "Add")
            }
        }
    }
}