package com.premiersolutionshi.support.domain;

import com.premiersolutionshi.common.domain.ModifiedDomain;

public class IssueComments extends ModifiedDomain {
    private static final long serialVersionUID = 369920365147970155L;

    private Integer issueFk;
    private String comments;

    public Integer getIssueFk() {
        return issueFk;
    }

    public void setIssueFk(Integer issueFk) {
        this.issueFk = issueFk;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getCharCount() {
        if (comments != null) {
            return comments.length();
        }
        return 0;
    }

    public int getLineBreakCount() {
        if (comments != null) {
            return comments.split("/\n/").length + 1;
        }
        return 0;
    }

    public boolean isTooLong() {
        return getLineBreakCount() > 10 || getCharCount() > 800;
    }

    public static void main(String[] args) {
        String comments = "-----Original Message----- \n" +
"From: Abney, Joshua B LS2 USS Farragut, USN Navy [mailto:joshua.abney@ddg99.navy.mil] \n" +
"Sent: Monday, December 10, 2018 10:03 AM \n" +
"To: Norman Newson <norman.newson@premiersolutionshi.com> \n" +
"Cc: Rivera, Gamalier LT, FAR SUPPO, DDG99, USN <gamalier.rivera@ddg99.navy.mil>; "
+ "Shultz, Dwayne L. LSC, DDG99, USN <dwayne.shultz@ddg99.navy.mil>; Thompson, Richard C LS1 USN, DDG99 <richard.thompson@ddg99.navy.mil>; Shannon, William P CTR COMNAVSURFLANT HQ, C43P113 <william.p.shannon2.ctr@navy.mil>; 'Mcnulty, Joseph A. MAJ (KEARSARGE, SUPPLY)' <mcnultyj@lhd3.navy.mil>; Coffman, Daniel G MSgt COMNAVSURFLANT HQ, N4122B <daniel.g.coffman@navy.mil>; Tiffaney Scott <tiffaney.scott@premiersolutionshi.com>" +
"Subject: RE: Facet Help \n" +
"\n" +
"Sir, \n" +
"\n" +
"Our facet computer is currently unable to burn our scans onto the facet disc. We have tried multiple discs and it hasn't worked. I'm not sure if it's the disc reader being messed up or if it's a problem with the discs we are using. \n" +
"\n" +
"V/R \n" +
"LS2 Abney\n" + 
"S-1 \n" +
"USS Farragut \n";
        String[] commentsArr = comments.trim().split("\n");
        System.out.println("line break count=" + (commentsArr.length + 1));
        System.out.println(comments);
    }
}
