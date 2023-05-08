package server.mainproject.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import server.mainproject.member.dto.MemberDto;
import server.mainproject.member.entity.Member;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {
    Member memberPostDtoToMember(MemberDto.Post requestBody);

    Member memberPatchDtoToMember(MemberDto.Patch requestBody);

    MemberDto.Response memberToMemberResponseDto(Member member);

    List<Member> membersToMemberReponseDtos(List<Member> members);
}
