package tkamul.ae.mdmcontrollers.domain.core.extentionFunction

import tkamul.ae.mdmcontrollers.domain.entities.PackageInfo

object  PackageNameExt {
    /**
     * @return true if incoming package name  from socket exist in device , false if its not matching any
     */
 fun <E> MutableList<E>.containsApp(packageName: String): Boolean {
        for (child in this){
            child as String
            if (child.toPackageInfo().packageName.equals(packageName,true))
                return true
        }
        return false
    }
}

/**
 * convert device installed packages from MutableList<String> to List<PackageInfo>
 * @return List<PackageInfo>
 */
fun  MutableList<String>.toPackageInfoList(): List<PackageInfo> {
    val packList = mutableListOf<PackageInfo>()
    for (child in this )
        packList.add(child.toPackageInfo())
    return  packList.toList()

}

/**
 * every child came from mobiiot packageList method list contain :
 * AppType:ThirdApp\n\nPackageName:numan.altkamul\n\nAppName:AlTkamul\n\nVersion:1.2.1.8\n\n
 * so this function convert mentioned string to PackageInfo
 */
 fun String.toPackageInfo(): PackageInfo {
    val appType = this.substring(this.indexOf("AppType")+"AppType:".length , this.indexOf("PackageName")).trim()
    val packageName = this.substring(this.indexOf("PackageName")+"PackageName:".length , this.indexOf("AppName")).trim()
    val appName = this.substring(this.indexOf("AppName")+"AppName:".length , this.indexOf("Version")).trim()
    val version = this.substring(this.indexOf("Version")+"Version:".length).trim()
    return PackageInfo(appType , packageName , appName , version)
}
