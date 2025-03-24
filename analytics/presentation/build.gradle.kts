plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.gapps.analytics.presentation"
}

dependencies {
    implementation(projects.analytics.domain)
}