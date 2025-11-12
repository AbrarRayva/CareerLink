package com.elevatestudio.careerlink.ui.screen.lowongan

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elevatestudio.careerlink.ui.components.ConfirmationDialog
import com.elevatestudio.careerlink.ui.components.PrimaryButton
import com.elevatestudio.careerlink.ui.components.SuccessDialog
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjukanLowonganScreen(
    lowonganId: String,
    viewModel: LowonganViewModel = viewModel(),
    onBackClick: () -> Unit,
    onGoToHome: () -> Unit
) {
    val state by viewModel.lamaranState.collectAsState()
    val submissionState by viewModel.submissionState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showKirimDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val maxFileSize = 10 * 1024 * 1024

    LaunchedEffect(submissionState) {
        when (val S = submissionState) {
            is SubmissionState.Error -> {
                snackbarHostState.showSnackbar(
                    message = S.message,
                    actionLabel = "Tutup"
                )
                viewModel.resetSubmissionState()
            }
            else -> {}
        }
    }

    val cvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileSize = context.contentResolver.openFileDescriptor(uri, "r")?.statSize ?: 0
            if (fileSize > maxFileSize) {
                scope.launch {
                    snackbarHostState.showSnackbar("File CV terlalu besar! Maksimal 10MB.")
                }
            } else {
                viewModel.onLamaranEvent(LamaranFormEvent.CvUploaded(uri.toString()))
            }
        }
    }

    val portofolioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileSize = context.contentResolver.openFileDescriptor(uri, "r")?.statSize ?: 0
            if (fileSize > maxFileSize) {
                scope.launch {
                    snackbarHostState.showSnackbar("File Portofolio terlalu besar! Maksimal 10MB.")
                }
            } else {
                viewModel.onLamaranEvent(LamaranFormEvent.PortofolioUploaded(uri.toString()))
            }
        }
    }

    val rekomenLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileSize = context.contentResolver.openFileDescriptor(uri, "r")?.statSize ?: 0
            if (fileSize > maxFileSize) {
                scope.launch {
                    snackbarHostState.showSnackbar("File Surat Rekomendasi terlalu besar! Maksimal 10MB.")
                }
            } else {
                viewModel.onLamaranEvent(LamaranFormEvent.SuratRekomendasiUploaded(uri.toString()))
            }
        }
    }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = { Text("Ajukan Lowongan") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            PrimaryButton(
                text = "KIRIM LAMARAN",
                onClick = {
                    showKirimDialog = true
                },
                enabled = state.isFormValid && (submissionState != SubmissionState.Loading),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Bagian 1: Data Diri (Tetap sama)
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Harap Lengkapi Data Diri Anda", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = state.namaLengkap,
                            onValueChange = { viewModel.onLamaranEvent(LamaranFormEvent.NamaChanged(it)) },
                            label = { Text("Nama Lengkap") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = state.tanggalLahir,
                            onValueChange = { viewModel.onLamaranEvent(LamaranFormEvent.TanggalLahirChanged(it)) },
                            label = { Text("Tanggal Lahir") },
                            placeholder = { Text("DD/MM/YYYY") },
                            trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        FormDropdownMenu(
                            label = "Jenis Kelamin",
                            options = listOf("Pria", "Wanita"),
                            selectedOption = state.jenisKelamin,
                            onOptionSelected = { viewModel.onLamaranEvent(LamaranFormEvent.JenisKelaminChanged(it)) }
                        )

                        FormDropdownMenu(
                            label = "Pendidikan",
                            options = listOf("S1", "D3"),
                            selectedOption = state.pendidikan,
                            onOptionSelected = { viewModel.onLamaranEvent(LamaranFormEvent.PendidikanChanged(it)) }
                        )

                        OutlinedTextField(
                            value = state.programStudi,
                            onValueChange = { viewModel.onLamaranEvent(LamaranFormEvent.ProgramStudiChanged(it)) },
                            label = { Text("Program Studi") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = state.nomorAktif,
                            onValueChange = { viewModel.onLamaranEvent(LamaranFormEvent.NomorAktifChanged(it)) },
                            label = { Text("Nomor Aktif") },
                            placeholder = { Text("+62") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = state.ceritakanDirimu,
                            onValueChange = { viewModel.onLamaranEvent(LamaranFormEvent.CeritakanDirimuChanged(it)) },
                            label = { Text("Ceritakan Dirimu") },
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            supportingText = {
                                val textColor = if (state.wordCount >= 80) PrimaryGreen else Color.Gray
                                Text(
                                    text = "Minimal 80 kata (Tertulis: ${state.wordCount} kata)",
                                    color = textColor,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }
                        )
                    }
                }
            }

            // Bagian 2: Dokumen
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Harap Lengkapi Dokumen", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(16.dp))

                        val namaPlaceholder = if (state.namaLengkap.isBlank()) "NamaLengkap" else state.namaLengkap.replace(' ', '_')

                        FileUploadBox(
                            title = "Upload CV (.pdf, max 10MB)",
                            subtitle = "Upload CV terbaru dan relevan (Wajib)",
                            ketentuanNamaFile = "Ketentuan nama file: CV_$namaPlaceholder.pdf",
                            fileUri = state.cvUri,
                            isUploaded = state.cvUri != null,
                            onClick = { cvLauncher.launch("application/pdf") },
                            onClearClick = { viewModel.onLamaranEvent(LamaranFormEvent.ClearCv) } // <-- Panggil event Clear
                        )
                        FileUploadBox(
                            title = "Upload Portofolio (.pdf, max 10MB)",
                            subtitle = "Upload portofolio terbaru dan relevan",
                            ketentuanNamaFile = "Ketentuan nama file: Portofolio_$namaPlaceholder.pdf",
                            fileUri = state.portofolioUri,
                            isUploaded = state.portofolioUri != null,
                            onClick = { portofolioLauncher.launch("application/pdf") },
                            onClearClick = { viewModel.onLamaranEvent(LamaranFormEvent.ClearPortofolio) } // <-- Panggil event Clear
                        )
                        FileUploadBox(
                            title = "Upload Surat Rekomendasi (.pdf, max 10MB)",
                            subtitle = "Upload surat rekomendasi dari jurusan atau dosen terkait",
                            ketentuanNamaFile = "Ketentuan nama file: SuratRekomendasi_$namaPlaceholder.pdf",
                            fileUri = state.suratRekomendasiUri,
                            isUploaded = state.suratRekomendasiUri != null,
                            onClick = { rekomenLauncher.launch("application/pdf") },
                            onClearClick = { viewModel.onLamaranEvent(LamaranFormEvent.ClearSuratRekomendasi) } // <-- Panggil event Clear
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) } // Spacer buat tombol
        }

        if (showKirimDialog) {
            ConfirmationDialog(
                onDismiss = { showKirimDialog = false },
                onConfirm = {
                    showKirimDialog = false
                    viewModel.onLamaranEvent(LamaranFormEvent.Submit)
                },
                title = "Apakah pengisian data sudah benar dan lamaran siap kirim?",
                icon = {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Konfirmasi",
                        tint = PrimaryGreen,
                        modifier = Modifier.size(48.dp)
                    )
                }
            )
        }
        if (submissionState == SubmissionState.Loading) {
            Dialog(onDismissRequest = {}) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Mengirim lamaran...")
                    }
                }
            }
        }
        if (submissionState == SubmissionState.Success) {
            SuccessDialog(
                onDismiss = {
                    viewModel.resetSubmissionState()
                    onGoToHome()
                },
                onGoToHome = {
                    viewModel.resetSubmissionState()
                    onGoToHome()
                }
            )
        }
    }
}

// Composable untuk Dropdown (Tidak berubah)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDropdownMenu(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        isExpanded = false
                    }
                )
            }
        }
    }
}


// --- FUNGSI FILEUPLOADBOX DIPERBARUI ---
@Composable
fun FileUploadBox(
    title: String,
    subtitle: String,
    ketentuanNamaFile: String,
    fileUri: String?,
    isUploaded: Boolean,
    onClick: () -> Unit,
    onClearClick: () -> Unit // <-- TAMBAHAN PARAMETER
) {
    val context = LocalContext.current

    val displayText = if (isUploaded && fileUri != null) {
        getFileName(context, Uri.parse(fileUri))
    } else {
        title.substringBefore(" (")
    }

    Text(title, fontWeight = FontWeight.Bold)
    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

    Text(
        text = ketentuanNamaFile,
        style = MaterialTheme.typography.bodySmall,
        color = PrimaryGreen,
        fontWeight = FontWeight.SemiBold
    )

    Spacer(modifier = Modifier.height(8.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(1.dp, SecondaryGreen, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() } // Klik di mana aja di box buat milih file
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.UploadFile,
                contentDescription = null,
                tint = if (isUploaded) PrimaryGreen else SecondaryGreen
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = displayText,
                color = if (isUploaded) PrimaryGreen else Color.Gray,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f), // Bikin teks ngisi ruang
                maxLines = 2 // Kalo nama filenya panjang
            )

            // --- TOMBOL 'X' BARU ---
            if (isUploaded) {
                IconButton(onClick = onClearClick) { // Panggil event onClearClick
                    Icon(
                        Icons.Default.Clear, // Ikon 'X'
                        contentDescription = "Hapus file",
                        tint = Color.Gray
                    )
                }
            }
            // --- SELESAI TOMBOL 'X' ---
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

// --- FUNGSI HELPER (Tidak berubah) ---
private fun getFileName(context: Context, uri: Uri): String {
    var name: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }
    }
    return name ?: uri.lastPathSegment ?: "unknown_file"
}