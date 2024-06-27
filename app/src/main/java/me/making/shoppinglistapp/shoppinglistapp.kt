package me.making.shoppinglistapp
import android.drm.DrmStore.RightsStatus
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

data class ShoppingItems(val serialNo:Int, var Name:String, var contity:Int, var isEditing:Boolean );
@Composable

fun  shoppingList(){
    var Items by remember{ mutableStateOf(listOf<ShoppingItems>()) }
    var showlist by remember{ mutableStateOf(false) };
    var itemName by remember{ mutableStateOf("") };
    var itemQuantity by remember{ mutableStateOf("") };

    Column ( modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center

        )
    {
        Button(onClick = { showlist=true },modifier=Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Add Item", );
        }
        LazyColumn(modifier= Modifier
            .fillMaxSize()
            .padding(16.dp)){

            items(Items){
                item ->
                if(item.isEditing) {
                    shoppingListediting(
                        item = item,
                        onEditingComplete = { editedName, editedQuantity ->
                            Items = Items.map { it.copy(isEditing = false) }
                            val editedItem = Items.find { it.serialNo == item.serialNo }
                            editedItem?.let {
                                it.Name = editedName;
                                it.contity = editedQuantity;
                            }
                        })
                }
                    else{
                        shoppingListAdding(Items = item, onEditingClick = {

                            Items=Items.map { it.copy(isEditing=it.serialNo==item.serialNo) };

                        }
                            , onDeletingClick = {
                                Items=Items-item;
                            });

                    }

            }

        }
    }
    if(showlist){
        AlertDialog(onDismissRequest = { showlist=false ; itemName="";itemQuantity="" },
            confirmButton = {

                            Row( modifier= Modifier
                                .fillMaxWidth(),
                           horizontalArrangement = Arrangement.SpaceBetween
                                ){

                                Button(onClick = {
                            if(itemName.isNotBlank()) {
                                val newitem = ShoppingItems(
                                    Items.size + 1, itemName, itemQuantity.toInt(), false
                                )
                                Items = Items + newitem;
                                    showlist = false;
                                    itemName = "";
                                    itemQuantity = "";
                                }
                            }){
                                    Text(text = "Enter");
                                }
                                Button(onClick={showlist=false;itemName="";itemQuantity=""}){
                                    Text(text = "Cancel");
                                }
                            }
            },
            title = { Text(text = "Add Items", modifier = Modifier.padding(8.dp)) },
            text = {

               Column {


                    OutlinedTextField(
                        value = itemName, onValueChange = { itemName = it },
                        singleLine = true,
                        label = { Text(text = "Item Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),


                    )
               Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = itemQuantity, onValueChange = { itemQuantity = it },
                        singleLine = true,
                        label = { Text(text = "Item Quantity") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )


                }
            }

            )

    }

}
@Composable
fun shoppingListediting(item:ShoppingItems, onEditingComplete:(String,Int) ->Unit){
    var isEditing by remember{ mutableStateOf(item.isEditing) };
    var editedName by remember{ mutableStateOf(item.Name) };
    var editedQuantity by remember{ mutableStateOf(item.contity.toString()) };

 Row(
     Modifier
         .fillMaxWidth()
         .background(Color.White)
         .padding(8.dp),
     horizontalArrangement = Arrangement.SpaceEvenly,
 )

 {
     Column {
         BasicTextField(value = editedName,
             onValueChange = {editedName=it } ,
             Modifier
                 .padding(8.dp)
                 .wrapContentSize(),
             singleLine = true,

             )

         BasicTextField(value = editedQuantity,
             onValueChange = {editedQuantity = it } ,
             Modifier
                 .padding(8.dp)
                 .wrapContentSize(),
             singleLine = true,

             )
     }
     Button(onClick = {
         onEditingComplete(editedName, editedQuantity.toIntOrNull()?:1)
     }


     ) {
         Text(text = "save");
     }
 }


}

@Composable
fun shoppingListAdding(Items:ShoppingItems, onEditingClick: () -> Unit , onDeletingClick:()->Unit){


                Row ( modifier= Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(2.dp, color = Color(0XFF018786)),
                        shape = RoundedCornerShape(20)
                    )



                ){
                  Text(text = "${Items.Name} ",Modifier.padding(8.dp));
                    Text(text="${Items.contity}",Modifier.padding(8.dp));
                Spacer(modifier = Modifier.width(120.dp))
                    Row(Modifier.padding(8.dp)){

                        IconButton(onClick = onEditingClick) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                        }

                        IconButton(onClick = onDeletingClick) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }


                    }
                }

}
@Preview(showBackground = true)
@Composable

fun shoppingListPreview( ){

    shoppingList()
}
