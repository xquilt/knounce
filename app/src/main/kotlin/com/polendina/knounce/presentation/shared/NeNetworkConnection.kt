package com.polendina.knounce.presentation.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.polendina.knounce.R

@Composable
fun NoConnectionComposable(
    onRetryCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.noconnectivity),
            contentDescription = null,
        )
        Text(
            text = stringResource(R.string.no_network_interface),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = stringResource(R.string.no_network_connection_description),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(
            onClick = onRetryCallback,
            modifier = Modifier
                .wrapContentHeight()
                .width(150.dp)
        ) {
            Text(
                text = stringResource(id = R.string.retry),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

