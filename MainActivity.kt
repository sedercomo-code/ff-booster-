package com.ffbooster.pro

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StatFs
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.content.pm.PackageManager
import dev.rikka.shizuku.Shizuku
import java.util.Locale

class MainActivity : Activity() {
    private lateinit var status: TextView
    private lateinit var ram: TextView
    private lateinit var storage: TextView
    private val requestCode = 4107

    private val permissionListener = Shizuku.OnRequestPermissionResultListener { code, result ->
        if (code == requestCode) {
            updateShizukuStatus()
            Toast.makeText(this, if (result == PackageManager.PERMISSION_GRANTED) "Permissão Shizuku concedida" else "Permissão não concedida", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shizuku.addRequestPermissionResultListener(permissionListener)
        buildScreen()
        refreshStats()
        updateShizukuStatus()
    }

    override fun onDestroy() {
        Shizuku.removeRequestPermissionResultListener(permissionListener)
        super.onDestroy()
    }

    private fun buildScreen() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 34, 28, 24)
            setBackgroundColor(0xFF101318.toInt())
        }
        fun label(text: String, size: Float, color: Int = 0xFFFFFFFF.toInt(), bold: Boolean = false): TextView =
            TextView(this).apply {
                this.text = text
                textSize = size
                setTextColor(color)
                if (bold) setTypeface(null, android.graphics.Typeface.BOLD)
                setPadding(0, 10, 0, 10)
            }
        fun button(text: String, action: () -> Unit) = Button(this).apply {
            this.text = text
            isAllCaps = false
            setOnClickListener { action() }
        }

        layout.addView(label("FF BOOSTER PRO", 26f, 0xFF72F1A6.toInt(), true))
        layout.addView(label("Painel seguro para Free Fire • Android 15 / HyperOS", 14f, 0xFFB8C0CC.toInt()))
        status = label("Shizuku: verificando…", 16f, 0xFFFFFFFF.toInt(), true)
        layout.addView(status)
        layout.addView(button("Conectar / autorizar Shizuku") { requestShizukuPermission() })
        layout.addView(label("MONITOR DO APARELHO", 18f, 0xFF72F1A6.toInt(), true))
        ram = label("RAM: —", 16f)
        storage = label("Armazenamento: —", 16f)
        layout.addView(ram)
        layout.addView(storage)
        layout.addView(button("Atualizar monitor") { refreshStats() })
        layout.addView(label("PERFIL FREE FIRE", 18f, 0xFF72F1A6.toInt(), true))
        layout.addView(label("Este app não altera mira, dano, sensibilidade interna ou FPS por mágica. Ele ajuda a conferir recursos e abrir o jogo; o Android controla o que apps comuns podem modificar.", 14f, 0xFFB8C0CC.toInt()))
        layout.addView(button("Abrir Free Fire") { launchGame("com.dts.freefireth", "com.dts.freefiremax") })
        layout.addView(button("Como preparar o celular") {
            Toast.makeText(this, "Feche apps que você não está usando, desative economia de bateria para o jogo e evite jogar enquanto carrega.", Toast.LENGTH_LONG).show()
        })
        layout.addView(label("Shizuku não faz overclock nem remove limites térmicos. Nenhum serviço essencial será encerrado automaticamente.", 12f, 0xFF8D98A8.toInt()))
        setContentView(layout)
    }

    private fun requestShizukuPermission() {
        try {
            if (!Shizuku.pingBinder()) {
                Toast.makeText(this, "Abra o Shizuku e inicie o serviço primeiro.", Toast.LENGTH_LONG).show()
                return
            }
            when {
                Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED ->
                    Toast.makeText(this, "Shizuku já autorizado.", Toast.LENGTH_SHORT).show()
                Shizuku.shouldShowRequestPermissionRationale() ->
                    Shizuku.requestPermission(requestCode)
                else -> Shizuku.requestPermission(requestCode)
            }
            updateShizukuStatus()
        } catch (e: Exception) {
            Toast.makeText(this, "Não foi possível consultar o Shizuku: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateShizukuStatus() {
        val active = try { Shizuku.pingBinder() } catch (_: Exception) { false }
        val granted = try {
            active && Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
        } catch (_: Exception) { false }
        status.text = when {
            granted -> "Shizuku: conectado e autorizado"
            active -> "Shizuku: ativo, falta autorizar"
            else -> "Shizuku: não iniciado"
        }
    }

    private fun refreshStats() {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = ActivityManager.MemoryInfo()
        manager.getMemoryInfo(info)
        val total = info.totalMem / (1024.0 * 1024.0 * 1024.0)
        val free = info.availMem / (1024.0 * 1024.0 * 1024.0)
        ram.text = String.format(Locale.getDefault(), "RAM disponível: %.2f GB de %.2f GB", free, total)
        val stat = StatFs(filesDir.absolutePath)
        val freeBytes = stat.availableBytes / (1024.0 * 1024.0 * 1024.0)
        val totalBytes = stat.totalBytes / (1024.0 * 1024.0 * 1024.0)
        storage.text = String.format(Locale.getDefault(), "Armazenamento livre: %.1f GB de %.1f GB", freeBytes, totalBytes)
    }

    private fun launchGame(vararg packages: String) {
        for (pkg in packages) {
            val launch = packageManager.getLaunchIntentForPackage(pkg)
            if (launch != null) {
                startActivity(launch)
                return
            }
        }
        Toast.makeText(this, "Free Fire não encontrado. Instale/atualize o jogo pela loja oficial.", Toast.LENGTH_LONG).show()
    }
}
