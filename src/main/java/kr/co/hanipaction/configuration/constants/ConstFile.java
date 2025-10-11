package kr.co.hanipaction.configuration.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "constants.file")
@RequiredArgsConstructor
@ToString
@Getter
public class ConstFile {
    public final String uploadDirectory;
    public final String ContactPic;
    public final int maxPicCount;
}
