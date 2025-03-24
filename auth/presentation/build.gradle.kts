plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.gapps.auth.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}