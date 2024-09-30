import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.purpleye.taskease.R
import com.purpleye.taskease.data.database.Task
import com.purpleye.taskease.util.DateFormatUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteLazyColumn(
    items: List<Task>,
    onClick: (Task) -> Unit,
    onItemRemoved: (Task) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    var itemList by remember { mutableStateOf(items) }

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = itemList, key = {item -> item.id}) { item ->
            val dismissState = rememberSwipeToDismissBoxState()

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = { DismissBackground(dismissState) },
                content = {
                    TaskItem(
                        task = item,
                        onClick = onClick
                    )
                },
                enableDismissFromStartToEnd = false
            )

            // 削除されたときにアイテムをリストから削除
            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                LaunchedEffect(item) {
                    // スワイプ完了後のアクション（削除）
                    onItemRemoved(item)
                    itemList = itemList.toMutableList().also { it.remove(item) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = if (dismissState.dismissDirection  == SwipeToDismissBoxValue.EndToStart) Color.Red.copy(alpha = 0.6f) else Color.Transparent

    Card(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
            .fillMaxSize(),
        shape = CutCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
        colors = CardDefaults. cardColors(
            containerColor = color
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_large)),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_small))
            .clickable { onClick(task) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = CutCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
        colors = CardDefaults. cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            // Task name
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = task.task,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                // Dead Line
                Row(
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "date",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(dimensionResource(id = R.dimen.padding_extra_small)),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(text = DateFormatUtil.dateToStrong(task.deadLine))
                }
                
                Spacer(modifier = Modifier.weight(1f))

                // Priority
                Row(
                    modifier = Modifier.width(120.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    for (i in 1..task.priority) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star $i",
                            modifier = Modifier
                                .size(20.dp),
                            tint = colorResource(id = R.color.star_yellow)
                        )
                    }
                }
            }
        }
    }
}